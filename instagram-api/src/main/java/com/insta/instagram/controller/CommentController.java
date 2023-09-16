package com.insta.instagram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insta.instagram.exceptions.CommentException;
import com.insta.instagram.exceptions.PostException;
import com.insta.instagram.exceptions.UserException;
import com.insta.instagram.modal.Comment;
import com.insta.instagram.modal.User;
import com.insta.instagram.service.CommentService;
import com.insta.instagram.service.PostService;
import com.insta.instagram.service.UserService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

	@Autowired
	CommentService commentService;
	
	@Autowired
	UserService userService;
	
	@Autowired 
	PostService postService;
	
	@PostMapping("/create/{postId}")
	public ResponseEntity<Comment> createcommentHandler(@RequestBody Comment comment,@RequestHeader("Authorization") String token , @PathVariable Integer postId) throws UserException, PostException{
		
		User user = userService.findUserProfile(token);
		Comment createdComment = commentService.createComment(comment, postId, user.getId());
		return new ResponseEntity<Comment>(createdComment,HttpStatus.CREATED);
		
	}
	
	@PutMapping("/like/{CommentId}")
	public ResponseEntity<Comment> likeCommentHandler(@RequestHeader("Authorization")String token ,@PathVariable Integer CommentId) throws UserException, CommentException{
		User user = userService.findUserProfile(token);
		Comment likecomment = commentService.likeComment(CommentId, user.getId());
		
		return new ResponseEntity<Comment>(likecomment,HttpStatus.OK);
		
	}
	
	@PutMapping("/unlike/{CommentId}")
	public ResponseEntity<Comment> unlikeCommentHandler(@RequestHeader("Authorization")String token ,@PathVariable Integer CommentId) throws UserException, CommentException{
		User user = userService.findUserProfile(token);
		Comment likecomment = commentService.unlikeComment(CommentId, user.getId());
		
		return new ResponseEntity<Comment>(likecomment,HttpStatus.OK);
		
	}
	
    @GetMapping("/all/{CommentId}")
    public ResponseEntity<Comment> findCommentById(@PathVariable Integer CommentId) throws CommentException{
    	Comment comment = commentService.findCommentById(CommentId);
    	return new ResponseEntity<Comment>(comment,HttpStatus.OK);
    }
	
}
