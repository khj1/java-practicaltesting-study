package sample.cafekiosk.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 전체 테스트를 돌릴 때 @SpringBootTest 어노테이션이 붙은 테스트를 실행할 때 마다
 * SpringBoot를 새로 띄우는 것은 비효율적이다.
 * 테스트 시 한번만 띄울 수 있게 상속을 사용한다.
 */
@ActiveProfiles("test")
@SpringBootTest(properties = "spring.profiles.active=test")
public abstract class IntegrationTestSupport {
}
