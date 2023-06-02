package sample.cafekiosk.spring.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;
import sample.cafekiosk.spring.domain.product.Product;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

	@Id
	@Column(name = "order_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private int totalPrice;

	private LocalDateTime registerDateTime;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderProduct> orderProducts = new ArrayList<>();

	private Order(final List<Product> products, final LocalDateTime registerDateTime) {
		this.orderStatus = OrderStatus.INIT;
		this.totalPrice = calculateTotalPrice(products);
		this.registerDateTime = registerDateTime;
		this.orderProducts = products.stream()
				.map(product -> new OrderProduct(this, product))
				.collect(Collectors.toList());
	}

	public static Order create(final List<Product> products, final LocalDateTime registerDateTime) {
		return new Order(products, registerDateTime);
	}

	private int calculateTotalPrice(final List<Product> products) {
		return products.stream()
				.mapToInt(Product::getPrice)
				.sum();
	}
}
