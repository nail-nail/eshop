package id.ac.ui.cs.advprog.eshop;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EshopApplicationTests {



    @Test
    void main_shouldInvokeSpringApplicationRun() {
        String[] args = new String[] {"--test"};

        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(EshopApplication.class, args)).thenReturn(null);
            assertDoesNotThrow(() -> EshopApplication.main(args));
            mocked.verify(() -> SpringApplication.run(EshopApplication.class, args));
        }
    }
}
