package kr.ac.goldcow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.ac.goldcow.model.Comment;

public interface CommentRepositoryDataJpa extends JpaRepository<Comment, Long>{

}
