package alushkja.blogpad.posts.control;

import alushkja.blogpad.posts.entity.Post;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.RegistryType;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PostStore {

    @Inject
    @ConfigProperty(name = "root.storage.dir")
    String storageDir;

    @Inject
    @ConfigProperty(name = "minimum.storage.space", defaultValue = "50")
    int storageThreshold;

    @Inject
    TitleNormalizer normalizer;

    @Inject
    @RegistryType(type = MetricRegistry.Type.APPLICATION)
    MetricRegistry registry;

    Path storageDirectoryPath;

    @PostConstruct
    public void init() {
        this.storageDirectoryPath = Path.of(this.storageDir);
    }
//
    @Produces
    @Liveness
    public HealthCheck checkPostsDirectoryExists() {
        return () -> HealthCheckResponse.named("posts-directory-exists")
                .state(Files.exists(this.storageDirectoryPath))
                .build();
    }

    @Produces
    @Liveness
    public HealthCheck checkEnoughSpace() {
        var size = this.getPostsStorageSpaceInMB();
        var enoughSpace = size >= this.storageThreshold;
        return () -> HealthCheckResponse.named("posts-directory-has-space")
                .state(enoughSpace)
                .build();
    }

//    @Gauge(unit = "mb")
    public long getPostsStorageSpaceInMB() {
        try {
            return Files.getFileStore(this.storageDirectoryPath).getUsableSpace() / 1024 / 1024;
        } catch (IOException e) {
            throw new StorageException("Cannot fetch size information from " + this.storageDirectoryPath, e);
        }
    }

    public Post createNew(Post post) {
        var fileName = this.normalizer.normalize(post.title);
        if (this.fileExists(fileName)) {
            throw new BadRequestException("Post with name: " + fileName + " already exists, use PUT for updates");
        }
        post.setCreatedAt();
        post.fileName = fileName;
        var stringified = serialize(post);
        try {
            write(fileName, stringified);
            return post;
        } catch (IOException ex) {
            throw new StorageException("Cannot save post: " + fileName, ex);
        }
    }

    boolean fileExists(String fileName) {
        var fqn = this.storageDirectoryPath.resolve(fileName);
        return Files.exists(fqn);
    }

    public void update(Post post) {
        var fileName = this.normalizer.normalize(post.title);
        if (!this.fileExists(fileName)) {
            throw new BadRequestException("Post with name: " + fileName + " does not exists, use POST to create");
        }
        post.updateModifiedAt();
        var stringified = serialize(post);
        try {
            write(fileName, stringified);
        } catch (IOException ex) {
            throw new StorageException("Cannot save post: " + fileName, ex);
        }
    }

    String serialize(Post post) {
        var jsonb = JsonbBuilder.create();
        return jsonb.toJson(post);
    }

    void write(String fileName, String content) throws IOException {
        var path = this.storageDirectoryPath.resolve(fileName);
        Files.writeString(path, content);
    }

    public Post read(String title) {
        var fileName = this.normalizer.normalize(title);
        if (!this.fileExists(fileName)) {
            this.increaseNotExistingPostCounter();
            throw new StorageException("Cannot fetch post: " + fileName);
        }
        try {
            var stringified = this.readString(fileName);
            return deserialize(stringified);
        } catch (IOException ex) {
            throw new StorageException("Cannot fetch post: " + fileName, ex);
        }
    }

    void increaseNotExistingPostCounter() {
        registry.counter("fetch_post_with_ne_title").inc();
    }

    Post deserialize(String stringified) {
        var jsonb = JsonbBuilder.create();
        return jsonb.fromJson(stringified, Post.class);
    }

    String readString(String fileName) throws IOException {
        var path = this.storageDirectoryPath.resolve(fileName);
        return Files.readString(path);
    }

    public ConstraintResponse buildConstraintResponse(ConstraintViolationException violationExceptionSet, String errorMessage) {
        List<ConstraitError> errors = new ArrayList<>();
        for (ConstraintViolation<?> violations : violationExceptionSet.getConstraintViolations()) {
            var propertyPath = violations.getPropertyPath().toString();
            var message = violations.getMessage();
            var constraintType = violations.getConstraintDescriptor().getAttributes().toString();
            var invalidValue = violations.getInvalidValue().toString();
            errors.add(new ConstraitError(propertyPath, message, constraintType, invalidValue));
        }
        return new ConstraintResponse(errorMessage, errors);
    }
}
