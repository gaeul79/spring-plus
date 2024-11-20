package org.example.expert.domain.todo.entity

import jakarta.persistence.*
import lombok.Getter
import org.example.expert.domain.comment.entity.Comment
import org.example.expert.domain.common.entity.Timestamped
import org.example.expert.domain.manager.entity.Manager
import org.example.expert.domain.user.entity.User

@Getter
@Entity
@Table(name = "todos")
class Todo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    val title: String,

    @Column(nullable = false, length = 1000)
    val contents: String,

    @Column
    val weather: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
    ) : Timestamped() {
    @OneToMany(mappedBy = "todo", cascade = [CascadeType.REMOVE])
    val comments: MutableList<Comment> = mutableListOf()

    @OneToMany(mappedBy = "todo", cascade = [CascadeType.PERSIST])
    val managers: MutableList<Manager> = mutableListOf()

    // staic
    companion object {
        fun create(title: String, contents: String, weather: String, user: User): Todo {
            val todo = Todo(
                title = title,
                contents = contents,
                weather = weather,
                user = user
            )
            todo.addManager(Manager(user, todo))
            return todo
        }
    }

    fun addManager(manager: Manager) {
        managers.add(manager)
    }
}