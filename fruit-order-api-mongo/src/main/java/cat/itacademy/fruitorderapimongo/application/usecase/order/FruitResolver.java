package cat.itacademy.fruitorderapimongo.application.usecase.order;

import cat.itacademy.fruitorderapimongo.application.usecase.order.command.OrderItemCommand;
import cat.itacademy.fruitorderapimongo.domain.exception.ResourceNotFoundException;
import cat.itacademy.fruitorderapimongo.domain.model.Fruit;
import cat.itacademy.fruitorderapimongo.domain.model.order.OrderItem;
import cat.itacademy.fruitorderapimongo.domain.port.out.FruitRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FruitResolver {

    private final FruitRepositoryPort fruitRepository;

    public FruitResolver(FruitRepositoryPort fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    public List<OrderItem> resolve(List<OrderItemCommand> commands) {
        Map<String, Fruit> fruitsFound = validate(commands);
        return toOrderItems(commands, fruitsFound);
    }

    private Map<String, Fruit> validate(List<OrderItemCommand> commands) {
        List<String> fruitIds = commands.stream()
                .map(OrderItemCommand::fruitId)
                .distinct()
                .toList();

        Map<String, Fruit> fruitsFound = fruitRepository.findAllById(fruitIds)
                .stream()
                .collect(Collectors.toMap(Fruit::getId, fruit -> fruit));

        fruitIds.forEach(id -> {
            if (!fruitsFound.containsKey(id)) {
                throw new ResourceNotFoundException("Fruit", id);
            }
        });

        return fruitsFound;
    }

    private List<OrderItem> toOrderItems(List<OrderItemCommand> commands, Map<String, Fruit> fruitsFound) {
        return commands.stream()
                .map(item -> new OrderItem(fruitsFound.get(item.fruitId()), item.quantityInKg()))
                .toList();
    }
}