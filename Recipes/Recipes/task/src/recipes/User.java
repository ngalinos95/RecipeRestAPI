package recipes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @NotNull
    @Email(regexp = "[a-zA-Z0-9_]+@[a-zA-Z0-9]+\\.[a-zA-Z]+")
    @Column(name = "email")
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 8, message = "Password size too short")
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @Column(name = "role")
    private String role;
    @OneToMany(mappedBy = "userCreate")
    private Set<Recipe> recipeCollection;
}
