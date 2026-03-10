#### Naila Khadijah - AdvProg B
Modul 4:
1. Reflect based on Percival (2017) proposed self-reflective questions (in “Principles and Best 
Practice of Testing” submodule, chapter “Evaluating Your Testing Objectives”), whether this 
TDD flow is useful enough for you or not. If not, explain things that you need to do next time 
you make more tests.

Based on the TDD flow followed in this tutorial, TDD is quite useful because it forces me to understand the requirements before writing code. By writing tests first, I am more confident that the implementation already aligns with the specification. However, the challenge is that it requires high discipline to not immediately write the implementation, and I must truly understand the requirements before coding in order to write the tests. Going forward, I need to make sure every edge case is covered from the start, and to not skip the RED-GREEN-REFACTOR cycle.

2. You have created unit tests in Tutorial. Now reflect whether your tests have successfully 
followed F.I.R.S.T. principle or not. If not, explain things that you need to do the next time you 
create more tests. 

The tests created already sufficiently follow the F.I.R.S.T. principles:
- **Fast**: Each test runs in milliseconds because it uses mocks.
- **Independent**: Each test uses `@BeforeEach` so they do not depend on one another.
- **Repeatable**: Tests can be run in any environment because they do not depend on a database.
- **Self-validating**: Uses clear assertions (assertEquals, assertThrows, etc.).
- **Timely**: Tests are written before the implementation, in accordance with TDD.

The shortcoming is that some tests still need to be added for more complete edge case coverage.


Modul 3:
Reclection:
1. Explain what principles you apply to your project! 
I revisited the Car flow and enforced SRP in the repository by letting `update` only responsible for updating and changed so it delegate the lookup to `findById`, so there is only one place that knows how to locate a record. I also fixed DIP/OCP violation by wiring `CarController` to the `CarService` interface via constructor injection, which means the controller neither knows nor cares whether the underlying implementation is in‑memory or persistent. On the interface side I trimmed `deleteCarById` down to a more general `delete`, which keeps the service contract small enough that consumers only learn about the behaviors they truly need.

2. Explain the advantages of applying SOLID principles to your project with examples. 
Injecting `CarService` instead of `CarServiceImpl` means I can write controller tests by supplying a fake service, or later add a caching layer without touching the controller at all, which is the Open/Closed Principle in action. Centralizing the lookup inside `CarRepository.findById` prevents future contributors from patching bugs in multiple loops, so SRP and DRY reinforce each other and the `update` logic becomes less error‑prone. 

3. Explain the disadvantages of not applying SOLID principles to your project with examples. 
Before the refactor the controller referenced `CarServiceImpl` directly, so introducing a second implementation would require editing and retesting every controller that used cars, which is the exact opposite of OCP. The repository had two different loops that searched by ID, so a bug in the matching logic would have to be fixed in multiple places and would likely regress one of them, which shows us ignoring SRP multiplies maintenance cost. Lastly, exposing a very specific `deleteCarById` method forces every future service implementation—say, a batch delete—to implement semantics it may not care about, which is how bloated interfaces slow down development.

Modul 2:
deployment link: https://sound-lois-nail-nail-db1080b4.koyeb.app/

Reflection:
1. List the code quality issue(s) that you fixed during the exercise and explain your strategy on fixing them. 
The first issue I fixed is field level injection. I cleaned up the codebase by switching both `ProductController` and `ProductServiceImpl` to constructor injection, eliminating the field-level `@Autowired`. I also created the `PRODUCT_LIST_REDIRECT` constant to remove six duplicated literals. This is the second issue I fixed. I also removed the redundant `throws Exception` declarations from the functional Selenium tests and enabled JaCoCo XML reporting with a `sonar.coverage.jacoco.xmlReportPaths` configuration so SonarCloud now reflects the true coverage of my codebase. I removed unused import `org.openqa.selenium.NoSuchElementException`from my unit test. I also removed some empty methods for cleaner code and better clarity.

2. Look at your CI/CD workflows (GitHub)/pipelines (GitLab). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment? Explain the reasons (minimum 3 sentences)! 
Yes, I think it has met the definition of CI/CD. Every push or pull request automatically triggers the GitHub Actions workflow, which checks out the code, runs the full Gradle test suite, and publishes a SonarCloud analysis before merging, so we get rapid feedback on integration issues. This aligns with Continuous Integration definition. On the deployment side, the repo is linked to Koyeb via `koyeb.yaml`, so once CI succeeds and the change is pushed to `module-2-exercise`, Koyeb pulls the code, rebuilds the jar, and redeploys the service automatically. The process is still gated by source control, so any regression would be caught either by the CI tests or by the Koyeb health checks immediately after deployment.

Modul 1:
## Reflection 1
>You already implemented two new features using Spring Boot. Check again your source code
and evaluate the coding standards that you have learned in this module. Write clean code
principles and secure coding practices that have been applied to your code.  If you find any
mistake in your source code, please explain how to improve your code.

### Clean Code Principles Applied
1. **Meaningful names and focused abstractions**: Classes such as `ProductService`, `ProductRepository`, and helper methods like `renderEditPage` describe their intent and isolate responsibilities.
2. **Single responsibility functions**: `ProductController` delegates persistence to the service layer and funnels edit-page preparation into `renderEditPage`, keeping each method short and centered on one task.
3. **Avoiding duplication**: Shared logic (e.g., edit page preparation and `ProductRepository` ID assignment) is located in the same place, reducing the risk of inconsistent behavior for future changes.

### Secure Coding Practices Applied
1. All mutating actions (`/product/create`, `/product/edit`, `/product/delete/{id}`) are handled through POST requests and verify the supplied product ID before applying changes, preventing unintended GET-based state changes.
2. Product IDs are generated via UUIDs so users cannot enumerate predictable identifiers to tamper with other records.
3. The controller redirects back to `/product/list` whenever a requested product cannot be located, ensuring the application never renders a view with missing context.

### Areas to Improve
1. **Validate user input**: Controllers accept any quantity (even negative) or name, so adding validation annotations or methods would prevent negative or empty data from being persisted. Furthermore, inputs also should be sanitized to prevent injection.



# Reflection 2

> After writing the unit test, how do you feel? How many unit tests should be made in a
class? How to make sure that our unit tests are enough to verify our program? It would be
good if you learned about code coverage. Code coverage is a metric that can help you
understand how much of your source is tested. If you have 100% code coverage, does
that mean your code has no bugs or errors?
1.  Writing the model and repository tests gave me confidence that my project and the domain behaves as expected. I realized as much as tedious creating unit tests felt, it gave me insights I wouldn't have obtained if I didn't create the tests such as the fact that user will need to clear quantity input field before typing the number they want; small detail that matter. A class needs enough unit tests to cover its public behaviors, especially different branches. There is no fixed number, but every branch and critical rule should be exercised.
2. Code coverage helps reveal which branches remain untested. I learned from my previous course (DDP 2) that high coverage is useful but not a guarantee of bug-free. Cod-tests might miss assertions on real requirements or cover lines without verifying anything meaningful. So, in other words, tests quality matter as much as coverage, if not more.

>Suppose that after writing the CreateProductFunctionalTest.java along with the
corresponding test case, you were asked to create another functional test suite that
verifies the number of items in the product list. You decided to create a new Java class
similar to the prior functional test suites with the same setup procedures and instance
variables.  
What do you think about the cleanliness of the code of the new functional test suite? Will
the new code reduce the code quality? Identify the potential clean code issues, explain
the reasons, and suggest possible improvements to make the code cleaner!
1. Adding another functional test suite with the same setup would be redundant, as it would duplicate a lot of code, making maintenance harder and violating "Don’t Repeat Yourself". Changes to setup logic (e.g., new base path or credentials) would have to be updated in multiple classes. The duplication also hurts clarity because common concerns like input helpers or context resets are scattered. Extracting shared Selenium utilities/helpers or using an abstract base class for functional tests would keep setup in one place and keep each suite focused on its specific assertions.
