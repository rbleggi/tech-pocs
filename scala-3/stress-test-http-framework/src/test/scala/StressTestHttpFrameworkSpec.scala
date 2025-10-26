package com.rbleggi.stresstesthttpframework

class StressTestHttpFrameworkSpec {
  test("HttpStressTest should collect results for all requests") {
    val url = "https://httpbin.org/get"
    val requests = 5
    val concurrency = 2
    val test = new HttpStressTest(url, requests, concurrency)
    test.prepare()
    test.execute()
    val resultsField = test.getClass.getDeclaredField("results")
    resultsField.setAccessible(true)
    val results = resultsField.get(test).asInstanceOf[Seq[Long]]
    assert(results.size == requests)
    assert(results.forall(_ > 0))
  }
}
