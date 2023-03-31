package recipes;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.executable.ValidateOnExecution;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/recipe")
public class RecipesController {

    @Autowired
    RecipeService recipeService;

    @Autowired
    UserService userService;

    @PostMapping("/new")
    Map<String, Long> setRecipe(
            @Valid @RequestBody Recipe recipe,
            @AuthenticationPrincipal UserDetails details
    ) {
        User user = userService.getUserByEmail(details.getUsername());
        recipe.setUserCreate(user);
        long id = recipeService.saveRecipe(recipe);
        return Map.of("id", id);
    }

    @GetMapping("/{id}")
    Recipe getRecipe(@PathVariable int id) {
        Optional<Recipe> recipeById = recipeService.getRecipeById(id);
        if (recipeById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return recipeById.get();
    }

    @GetMapping("/search")
    List<Recipe> getCategoryListRecipes(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "name", required = false) String name
    ) {
        if (category != null && name == null) {
            return recipeService.getCategoryList(category);
        } else if (name != null && category == null) {
            return recipeService.getNameList(name);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateRecipe(
            @Valid @PathVariable long id,
            @Valid @RequestBody Recipe recipe,
            @AuthenticationPrincipal UserDetails details
    ) {
        Optional<Recipe> recipeById = recipeService.getRecipeById(id);
        if (recipeById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        String recipeBy = recipeById.get().getUserCreate().getEmail();
        if (!recipeBy.equals(details.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        User user = userService.getUserByEmail(details.getUsername());
        recipe.setUserCreate(user);
        recipeService.saveRecipe(recipe,id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteRecipe(
            @Valid @PathVariable long id,
            @AuthenticationPrincipal UserDetails details
    ) {
        Optional<Recipe> recipeById = recipeService.getRecipeById(id);
        if (recipeById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        String recipeBy = recipeById.get().getUserCreate().getEmail();
        if (!recipeBy.equals(details.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        recipeService.deleteRecipeById(id);
    }
}






