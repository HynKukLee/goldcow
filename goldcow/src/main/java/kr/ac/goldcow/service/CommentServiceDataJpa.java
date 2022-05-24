package kr.ac.goldcow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.goldcow.model.Comment;
import kr.ac.goldcow.repository.CommentRepositoryDataJpa;
@Service("commentService")
@Transactional
public class CommentServiceDataJpa implements CommentService {
	@Autowired
	private CommentRepositoryDataJpa repository;
	@Override
	public void saveComment(Comment comment) {
		// TODO Auto-generated method stub
		repository.save(comment);
	}

}
