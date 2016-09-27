package com.shape.web.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = "Todolist")
@EqualsAndHashCode(exclude = {"project", "user", "superTodolist"})
@ToString(exclude={"user","project","superTodolist"})
public class Todolist implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column(name = "TODOLISTIDX")
    private Integer todolistidx;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "USERIDX")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "PROJECTIDX")
    private Project project;


    @OneToMany(mappedBy = "superTodolist")
    private Set<Todolist> subTodolist= new HashSet<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "SUPERTODOLISTIDX")
    private Todolist superTodolist;


    @Column(name = "OK")
    private boolean ok = false;

    @NotNull
    @Column(name = "CONTENT", columnDefinition = "TEXT")
    private String content;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "STARTDATE")
    @Type(type = "date")
    private Date startdate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "ENDDATE")
    @Type(type = "date")
    private Date enddate;

    @Column(name = "CREATEDAT")
    private Date createdat;

    @Column(name = "UPDATEDAT")
    private Date updatedat;

    @PrePersist
    protected void onCreate() {
        updatedat = createdat = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedat = new Date();
    }

    public Todolist() {
    }

    public Todolist(String content, Date startdate, Date enddate) {
        this.content = content;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public void addsubTodolist(Todolist todo) {
        this.subTodolist.add(todo);
        todo.setSuperTodolist(this);
    }


}
