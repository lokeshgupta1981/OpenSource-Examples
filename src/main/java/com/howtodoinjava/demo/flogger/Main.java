package com.howtodoinjava.demo.flogger;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.IntStream;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.LazyArgs;
import com.google.common.flogger.StackSize;
import com.howtodoinjava.demo.lombok.Article;

public class Main {
	private static final FluentLogger logger = FluentLogger.forEnclosingClass();

	public static void main(String[] args) {
		
		//System.setProperty("flogger.backend_factory", "com.google.common.flogger.backend.log4j2.Log4j2BackendFactory#getInstance");

		logger.atWarning().log("Warning message");
		
		Article a = Article.builder(1L).title("Test Article").tag("Data").build();
		logger.atInfo().log("Article found : %s", a);
		
		logger.at(Level.SEVERE).atMostEvery(50, TimeUnit.SECONDS).log("SEVERE", LazyArgs.lazy(() -> doSomeCostly()));
		
		logger.atInfo()
			.withStackTrace(StackSize.SMALL)
			.withCause(new NullPointerException())
			.log("NullPointerException Received");
		
		
		IntStream.range(0, 50).forEach(value -> {
		    logger.atInfo().atMostEvery(1,TimeUnit.SECONDS).log("The counter is => %d", value);
		});

	}

	public static int doSomeCostly() {
		return 0;
	}
}
