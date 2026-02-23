#### Naila Khadijah - AdvProg B

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
