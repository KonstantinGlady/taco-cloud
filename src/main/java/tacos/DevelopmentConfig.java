package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.data.IngredientRepository;
import tacos.data.OrderRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

import java.util.Arrays;

@Profile("!prod")
@Configuration
public class DevelopmentConfig {

    @Bean
    public CommandLineRunner dataLoader(IngredientRepository repo, TacoRepository tacoRepo,
                                        UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            repo.deleteAll();
            userRepo.deleteAll();

            Ingredient flour = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
            Ingredient corn = new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP);
            Ingredient beef = new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN);
            Ingredient carnitas = new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN);
            Ingredient tomatoes = new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES);
            Ingredient lettuce = new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES);
            Ingredient cheddar = new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE);
            Ingredient jack = new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE);
            Ingredient salsa = new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE);
            Ingredient cream = new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE);

            repo.save(flour);
            repo.save(corn);
            repo.save(beef);
            repo.save(carnitas);
            repo.save(tomatoes);
            repo.save(lettuce);
            repo.save(cheddar);
            repo.save(jack);
            repo.save(salsa);
            repo.save(cream);

           Taco taco1 = new Taco();
            taco1.setName("Cornivore");
            taco1.setIngredients(Arrays.asList(
                    flour, beef, cheddar, cream));
            tacoRepo.save(taco1);

            Taco taco2 = new Taco();
            taco2.setName("Bovine Bounty");
            taco2.setIngredients(Arrays.asList(
                    corn, carnitas, jack, salsa));
            tacoRepo.save(taco2);

            Taco taco3 = new Taco();
            taco3.setName("Veg-Out");
            taco3.setIngredients(Arrays.asList(
                    corn, tomatoes, lettuce, jack, cream));
            tacoRepo.save(taco3);

            userRepo.save(new User("user", encoder.encode("1"), "user",
                    "street", "city", "st", "zip", "123"));
        };
    }
}
