package cz.muni.fi.pv260.productfilter;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"unchecked", "Duplicates"})
public class AtLeastNOfFilterTest {

	private AtLeastNOfFilter<Integer> filter;

	private interface TestFilter extends Filter<Integer> {
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorThrowsIllegalArgumentException() {
		filter = new AtLeastNOfFilter<>(-1);
	}

	@Test(expected = FilterNeverSucceeds.class)
	public void constructorThrowsFilterNeverSucceeds() {
		filter = new AtLeastNOfFilter<>(2, mock(TestFilter.class));
	}

	@Test
	public void filterPassesWhenAtLeastNPasses() {
		TestFilter filter1 = mock(TestFilter.class);
		TestFilter filter2 = mock(TestFilter.class);
		TestFilter filter3 = mock(TestFilter.class);

		doReturn(true)
				.when(filter1)
				.passes(anyInt());
		doReturn(true)
				.when(filter2)
				.passes(anyInt());
		doReturn(false)
				.when(filter3)
				.passes(anyInt());

		filter = new AtLeastNOfFilter<>(2, filter1, filter2, filter3);

		assertThat(filter.passes(1)).isTrue();
	}

	@Test
	public void filterFailsWhenAtMostNMinusOnePass() {
		TestFilter filter1 = new TestFilter() {
			@Override
			public boolean passes(Integer item) {
				return false;
			}
		};
		TestFilter filter2 = new TestFilter() {
			@Override
			public boolean passes(Integer item) {
				return false;
			}
		};
		TestFilter filter3 = new TestFilter() {
			@Override
			public boolean passes(Integer item) {
				return true;
			}
		};

		filter = new AtLeastNOfFilter<>(2, filter1, filter2, filter3);

		assertThat(filter.passes(1)).isFalse();
	}

}