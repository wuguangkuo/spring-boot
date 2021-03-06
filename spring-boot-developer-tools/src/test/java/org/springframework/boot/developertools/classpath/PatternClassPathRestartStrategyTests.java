/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.developertools.classpath;

import java.io.File;

import org.junit.Test;
import org.springframework.boot.developertools.filewatch.ChangedFile;
import org.springframework.boot.developertools.filewatch.ChangedFile.Type;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link PatternClassPathRestartStrategy}.
 *
 * @author Phillip Webb
 */
public class PatternClassPathRestartStrategyTests {

	@Test
	public void nullPattern() throws Exception {
		ClassPathRestartStrategy strategy = createStrategy(null);
		assertRestartRequired(strategy, "a/b.txt", true);
	}

	@Test
	public void emptyPattern() throws Exception {
		ClassPathRestartStrategy strategy = createStrategy("");
		assertRestartRequired(strategy, "a/b.txt", true);
	}

	@Test
	public void singlePattern() throws Exception {
		ClassPathRestartStrategy strategy = createStrategy("static/**");
		assertRestartRequired(strategy, "static/file.txt", false);
		assertRestartRequired(strategy, "static/folder/file.txt", false);
		assertRestartRequired(strategy, "public/file.txt", true);
		assertRestartRequired(strategy, "public/folder/file.txt", true);
	}

	@Test
	public void multiplePatterns() throws Exception {
		ClassPathRestartStrategy strategy = createStrategy("static/**,public/**");
		assertRestartRequired(strategy, "static/file.txt", false);
		assertRestartRequired(strategy, "static/folder/file.txt", false);
		assertRestartRequired(strategy, "public/file.txt", false);
		assertRestartRequired(strategy, "public/folder/file.txt", false);
		assertRestartRequired(strategy, "src/file.txt", true);
		assertRestartRequired(strategy, "src/folder/file.txt", true);
	}

	private ClassPathRestartStrategy createStrategy(String pattern) {
		return new PatternClassPathRestartStrategy(pattern);
	}

	private void assertRestartRequired(ClassPathRestartStrategy strategy,
			String relativeName, boolean expected) {
		assertThat(strategy.isRestartRequired(mockFile(relativeName)), equalTo(expected));
	}

	private ChangedFile mockFile(String relativeName) {
		return new ChangedFile(new File("."), new File("./" + relativeName), Type.ADD);
	}

}
