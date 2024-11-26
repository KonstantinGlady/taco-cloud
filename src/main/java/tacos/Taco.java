package tacos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Taco {

    @NotNull
    private long id;
    private Date createdAt = new Date();
    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;
    @NotNull
    @Size(min = 1, message = "Choose at least 1 ingredient")
    private List<IngredientRef> ingredients = new ArrayList<>();

    public void addIngredient(Ingredient taco) {
        ingredients.add(new IngredientRef(taco.getId()));
    }
}
