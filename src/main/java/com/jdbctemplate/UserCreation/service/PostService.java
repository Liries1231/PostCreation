package com.jdbctemplate.UserCreation.service;

import com.jdbctemplate.UserCreation.dto.PostCreateRequest;
import com.jdbctemplate.UserCreation.dto.PostDto;
import com.jdbctemplate.UserCreation.entity.PostEntity;
import com.jdbctemplate.UserCreation.repos.PostRepos;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {
    private final PostRepos postRepos;
    private final List<PostDto> cache = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final LinkedBlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void init() {
        executorService.submit(this::processQueue);
    }

    private void processQueue() {
        try {
            while (true) {
                Runnable task = taskQueue.take();
                task.run();
                Thread thread = new Thread(task);
                thread.sleep(5000);
                log.info("Задача выполнена, размер очереди: {}", taskQueue.size());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    public PostDto createPost(PostCreateRequest postCreateRequest) {
        taskQueue.offer(() -> {
            log.info("начало таска.....");

            PostEntity postEntity = PostMapper.toEntity(postCreateRequest);
            PostEntity savedPost = postRepos.save(postEntity);
            cache.add(PostMapper.toDto(savedPost));
            log.info("конец таска.....");

        });
        log.info("Размер очереди: {}", taskQueue.size());
        return new PostDto();


    }

    public void deletePost(Long postId) {
        postRepos.delete(postId);
        cache.add(PostMapper.toDto(postRepos.findById(postId)));
        updateCache();

    }

    public PostDto getPostById(Long postId) {
        PostEntity post = postRepos.findById(postId);
        if (post != null) {
            return new PostDto(post.getId(), post.getTitle(), post.getDescription());
        }
        return null;  // Или выбросить исключение, если нужно
    }

    public ArrayList<PostDto> getLastPosts() {


        List<PostEntity> latestPosts = postRepos.findLastPost(10);
        ArrayList<PostDto> postDtos = latestPosts.stream()
                .map(PostMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));

        updateCache();

        return postDtos;
    }

    public void updatePost(Long id, PostCreateRequest postEntity) {
        PostEntity post = postRepos.findById(id);
        if (post != null) {
            post.setDescription(postEntity.getDescription());
            post.setTitle(postEntity.getTitle());
            post.setUserId(postEntity.getUserId());
            postRepos.update(post);
        } else {
            throw new EntityNotFoundException("Post with id " + id + " not found");
        }
        updateCache();
    }


    @Scheduled(fixedRate = 300000)
    public void updateCache() {
        taskQueue.offer(() -> {
            logger.info("Новый кэш: {}", cache);
            List<PostEntity> posts = postRepos.findAll();
            cache.clear();
            List<PostDto> updatedCache = posts.stream()
                    .map(PostMapper::toDto)
                    .toList();
            cache.addAll(updatedCache);

        });
    }


}


