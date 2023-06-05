package sample.cafekiosk.learning;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

class GuavaLearningTest {

	@DisplayName("주어진 개수만큼 리스트를 쪼갠다.")
	@Test
	void partition() {
		// given
		List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

		// when
		List<List<Integer>> partition = Lists.partition(integers, 3);

		//then
		assertThat(partition).hasSize(2)
				.isEqualTo(List.of(
						List.of(1, 2, 3),
						List.of(4, 5, 6)
				));
	}

	@DisplayName("딱 나눠지지 않는 리스트를 쪼갤 때")
	@Test
	void partition2() {
		// given
		List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

		// when
		List<List<Integer>> partition = Lists.partition(integers, 4);

		//then
		assertThat(partition).hasSize(2)
				.isEqualTo(List.of(
						List.of(1, 2, 3, 4),
						List.of(5, 6)
				));
	}

	@DisplayName("멀티맵 추가 기능 확인")
	@Test
	void multiMap() {
		// given
		Multimap<String, String> multimap = ArrayListMultimap.create();
		multimap.put("커피", "아메리카노");
		multimap.put("커피", "카페라떼");
		multimap.put("커피", "카푸치노");
		multimap.put("베이커리", "크루아상");
		multimap.put("베이커리", "식빵");

		// when
		Collection<String> coffees = multimap.get("커피");

		//then
		assertThat(coffees).hasSize(3)
				.isEqualTo(List.of("아메리카노", "카페라떼", "카푸치노"));

	}

	@DisplayName("멀티맵 제거 기능 확인")
	@TestFactory
	Collection<DynamicTest> multiMapRemove() {
		// given
		Multimap<String, String> multimap = ArrayListMultimap.create();
		multimap.put("커피", "아메리카노");
		multimap.put("커피", "카페라떼");
		multimap.put("커피", "카푸치노");
		multimap.put("베이커리", "크루아상");
		multimap.put("베이커리", "식빵");

		return List.of(
				DynamicTest.dynamicTest("1개 value 삭제", () -> {
					// when
					multimap.remove("커피", "카푸치노");

					// then
					assertThat(multimap.get("커피")).hasSize(2)
							.isEqualTo(List.of("아메리카노", "카페라떼"));
				}),
				DynamicTest.dynamicTest("value 일괄 삭제", () -> {
					// when
					multimap.removeAll("커피");

					// then
					assertThat(multimap.get("커피")).isEmpty();
				}));
	}
}
