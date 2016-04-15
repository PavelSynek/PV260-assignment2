package cz.muni.fi.pv260.productfilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SuppressWarnings("Duplicates")
@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

	private Controller controller;
	@Mock
	Input mockInput;
	@Mock
	Output mockOutput;
	@Mock
	Logger mockLogger;
	@Mock
	Filter<Product> mockFilter;
	@Captor
	ArgumentCaptor<Collection<Product>> argumentCaptor;

	private interface ProductFilter extends Filter<Product> {
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorThrowsWhenInputIsNull() {
		controller = new Controller(null, mockOutput, mockLogger);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorThrowsWhenOutputIsNull() {
		controller = new Controller(mockInput, null, mockLogger);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorThrowsWhenLoggerIsNull() {
		controller = new Controller(mockInput, mockOutput, null);
	}

	@Test
	public void controllerReturnsRightItems() throws Exception {
		controller = new Controller(mockInput, mockOutput, mockLogger);
		Product product1 = mock(Product.class);
		Product product2 = mock(Product.class);
		Product product3 = mock(Product.class);
		List<Product> productList = Arrays.asList(product1, product2, product3);

		doReturn(productList)
				.when(mockInput)
				.obtainProducts();

		doReturn(true)
				.when(mockFilter)
				.passes(product1);
		doReturn(true)
				.when(mockFilter)
				.passes(product2);
		doReturn(false)
				.when(mockFilter)
				.passes(product3);

		controller.select(mockFilter);

		verify(mockOutput).postSelectedProducts(argumentCaptor.capture());
		assertThat(argumentCaptor.getValue()).containsExactly(product1, product2);
	}

	@Test
	public void loggerLogsRightValue() throws Exception {
		controller = new Controller(mockInput, mockOutput, mockLogger);
		Product product1 = mock(Product.class);
		Product product2 = mock(Product.class);
		Product product3 = mock(Product.class);
		List<Product> productList = Arrays.asList(product1, product2, product3);

		doReturn(productList)
				.when(mockInput)
				.obtainProducts();

		doReturn(true)
				.when(mockFilter)
				.passes(product1);
		doReturn(true)
				.when(mockFilter)
				.passes(product2);
		doReturn(false)
				.when(mockFilter)
				.passes(product3);

		controller.select(mockFilter);

		verify(mockLogger).setLevel("INFO");
		verify(mockLogger).log(Controller.class.getSimpleName(),
				"Successfully selected 2 out of 3 available products.");
	}

	@Test
	public void controllerFailsInRightWay() throws Exception {
		controller = new Controller(mockInput, mockOutput, mockLogger);
		ObtainFailedException expected = new ObtainFailedException();
		doThrow(expected)
				.when(mockInput)
				.obtainProducts();

		controller.select(mockFilter);

		verify(mockLogger).setLevel("ERROR");
		verify(mockLogger).log(Controller.class.getSimpleName(),
				"Filter procedure failed with exception: " + expected);
		verifyNoMoreInteractions(mockOutput);
	}
}