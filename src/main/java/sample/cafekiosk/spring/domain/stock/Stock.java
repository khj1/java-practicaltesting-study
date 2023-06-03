package sample.cafekiosk.spring.domain.stock;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock extends BaseEntity {

	@Id
	@Column(name = "stock_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String productNumber;

	private int quantity;

	@Builder
	public Stock(final String productNumber, final int quantity) {
		this.productNumber = productNumber;
		this.quantity = quantity;
	}

	public static Stock create(final String productNumber, final int quantity) {
		return Stock.builder()
				.productNumber(productNumber)
				.quantity(quantity)
				.build();
	}

	public boolean isQuantityLessThan(final int quantity) {
		return this.quantity < quantity;
	}

	/**
	 * 도메인에서 예외를 체크하는 것과 서비스 로직에서 예외를 체크하는 것은
	 * 아주 다른 케이스다. 같은 기능이라고 하더라도 발생할 수 있는 상황이 다를 수 있다.
	 * 또한 서비스와 도메인에서 발생하는 예외 메시지를 다르게 주고 싶을 수도 있다.
	 */
	public void deductQuantity(final int quantity) {
		if (isQuantityLessThan(quantity)) {
			throw new IllegalArgumentException("차감할 재고 수량이 없습니다.");
		}
		this.quantity -= quantity;
	}
}
