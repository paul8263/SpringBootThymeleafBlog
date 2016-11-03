package com.paultech.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by paulzhang on 27/10/2016.
 */
@Entity
public class BlogComment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentDate;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User commenter;
    @ManyToOne
    @JoinColumn(name = "BLOG_ID")
    private Blog commentedBlog;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }

    public Blog getCommentedBlog() {
        return commentedBlog;
    }

    public void setCommentedBlog(Blog commentedBlog) {
        this.commentedBlog = commentedBlog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogComment that = (BlogComment) o;

        if (id != that.id) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (commentDate != null ? !commentDate.equals(that.commentDate) : that.commentDate != null) return false;
        if (commenter != null ? !commenter.equals(that.commenter) : that.commenter != null) return false;
        return commentedBlog != null ? commentedBlog.equals(that.commentedBlog) : that.commentedBlog == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (commentDate != null ? commentDate.hashCode() : 0);
        result = 31 * result + (commenter != null ? commenter.hashCode() : 0);
        result = 31 * result + (commentedBlog != null ? commentedBlog.hashCode() : 0);
        return result;
    }
}
