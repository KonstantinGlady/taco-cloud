package tacos.data;

import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tacos.Ingredient;
import tacos.IngredientRef;
import tacos.Taco;
import tacos.TacoOrder;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.sql.Types.TIMESTAMP;
import static java.sql.Types.VARCHAR;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcOperations jdbcOperations;

    @Autowired
    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    @Transactional
    public TacoOrder save(TacoOrder order) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into Taco_Order (delivery_name, delivery_street, delivery_city, " +
                        "delivery_state, delivery_zip, " +
                        "cc_number, cc_expiration, cc_cvv, placed_at) values(?,?,?,?,?,?,?,?,?)",
                VARCHAR, VARCHAR, VARCHAR, VARCHAR, VARCHAR, VARCHAR, VARCHAR, VARCHAR, TIMESTAMP
        );

        pscf.setReturnGeneratedKeys(true);
        order.setPlacedAt(new Date());

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        order.getDeliveryName(),
                        order.getDeliveryStreet(),
                        order.getDeliveryCity(),
                        order.getDeliveryState(),
                        order.getDeliveryZip(),
                        order.getCcNumber(),
                        order.getCcExpiration(),
                        order.getCcCVV(),
                        order.getPlacedAt())
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);

        List<Taco> tacos = order.getTacos();
        int i = 0;
        for (Taco taco : tacos) {
            saveTaco(orderId, i++, taco);
        }
        return order;
    }

    private long saveTaco(long orderId, int orderKey, Taco taco) {
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into Taco (name, created_at, taco_order, taco_order_key) " +
                        "values (?,?,?,?)",
                VARCHAR, TIMESTAMP, Type.LONG, Type.LONG
        );

        pscf.setReturnGeneratedKeys(true);
        taco.setCreatedAt(new Date());

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        taco.getName(),
                        taco.getCreatedAt(),
                        orderId,
                        orderKey
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);

        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);
        saveIngredientsRef(tacoId, taco.getIngredients());

        return tacoId;
    }

    private void saveIngredientsRef(long tacoId, List<IngredientRef> ingredients) {
        int key = 0;
        for (IngredientRef ingredientRef : ingredients) {
            jdbcOperations.update(
                    "insert into Ingredient_Ref (ingredient, taco, taco_key) " +
                            "values(?, ?, ?)",
                    ingredientRef.getIngredient(), tacoId, key++
            );
        }
    }
}
