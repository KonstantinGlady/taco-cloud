package tacos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Table
public class TacoOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    private long id;
    private Date placedAt = new Date();
    @NotBlank(message = "Name is required")
    private String deliveryName;
    @NotBlank(message = "Street is required")
    private String deliveryStreet;
    @NotBlank(message = "City is required")
    private String deliveryCity;
    @NotBlank(message = "State is required")
    private String deliveryState;
    @NotBlank(message = "Zip is required")
    private String deliveryZip;
    //@CreditCardNumber(message = "Credit card number is not valid")
    private String ccNumber;
    @Pattern(regexp = "^(0[1-9]|1[0-2])(/)([2-9][0-9])$")
    private String ccExpiration;
    @Digits(integer = 3, fraction = 0, message = "CVV is invalid")
    private String ccCVV;

    private List<Taco> tacos = new ArrayList<>();

    public void addTaco(Taco taco) {
        tacos.add(taco);
    }
}
