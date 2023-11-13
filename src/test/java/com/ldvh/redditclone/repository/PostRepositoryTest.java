package com.ldvh.redditclone.repository;

import com.ldvh.redditclone.BaseTest;
import com.ldvh.redditclone.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest extends BaseTest {

    @Autowired
    private PostRepo postRepository;

    @Test
    public void shouldSavePost() {
        Post expectedPostObject = new Post(null, "First Post", "http://url.site", "Test",
                0, null, Instant.now(), null);
        Post actualPostObject = postRepository.save(expectedPostObject);
        assertThat(actualPostObject).usingRecursiveComparison()
                .ignoringFields("postId").isEqualTo(expectedPostObject);
    }

}
