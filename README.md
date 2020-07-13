# Instructions
## How to start
Run the command `docker-compose up` in the repository root.

> Make sure you have up to date versions of Docker and docker-compose installed before running. I'm running Docker version 19.03.6 and docker-compose version 1.25.5.

My docker setup uses multi-stage build. Running the command will build the entire solution from source, pulling down necessary dependencies and deploying the containers. Please give it some time to finish.

Once you see the `Attaching to boxinator_db_1, boxinator_backend_1, boxinator_frontend_1` in the log wait a few seconds more for log output from the containers. Then the application is ready. Try it out by visiting http://localhost:8080/ in a modern browser. Preferably chromium-based.

> I'm running Linux (Ubuntu 18.04.4) and have not been able to test the solution on other platforms. I'm hopeful but not positive that Docker will solve it anyway on other platforms, but please reach out if you have trouble. Depends on what the base images support. Maybe we can find alternatives.
>
> Worst case I can deploy the app with Heroku and Github pages. Then it would be runnable from any browser with no local setup. However I would need some extra time to make the necessary modifications. Let me know if you need it.

When you are done reviewing clean up by running `docker-compose down -v --rmi all`. This will remove the containers, final images, networks and volumes created by the solution. To clean intermediate images created by the build steps it is required to run `docker image prune`, answering yes to removing dangling images. They can also be removed manually.

### Running tests
Run the frontend tests by navigating to the boxinator-frontend subtree and execute `npm install` followed by `npm run test`. This assumes you have node 8 or later installed on your system.
> If you need to run the tests inside a docker container instead I can provide instructions.

For the boxinator-backend you execute the tests with `mvn clean test`. This requires that you have maven installed, as well as a compatible JDK. The target feature level of the app is Java 9. Please use a JDK for Java 9 or later.
> Once again I can provide instructions for running the tests in docker if there's any problem. My Dockerfile uses maven:3.6.3-jdk-11-slim image for the build step.

## Assumptions
Some design decisions were based on these assumptions.
* Max weight for a box is 100 kg (well above Postnords 20 kg limit) and lowest allowed weight is 1 gram.
* Shipping price is limited to two decimals (no bitcoins ;) That means the lowest possible price is 0.01.
* Receiver name in the database is no longer than 255 characters and can consist of any unicode letters, allowing Chinese characters for example. In the frontend name length is capped at 70 characters for aestetical reasons.
* Finally the end user is running a modern up to date chromium based browser. I have not tested with other browsers but it might work...
    - The browser needs to support (caniuse) /\p{L}+/u regex (unicode letters) for receiver name validation. Modern updated browsers support this.

## Clarifications
How I've interpreted the requirements, as well as rationale for agreed upon deviations.
* Color is presented as RGB but stored in hex format.
    - I asked if this was a correct interpretation and got an okay on storing however I wanted as long as I can motivate. Storing as a hex (string) value has the benefit of making the database table more compact.
    - If storing as RGB value another table and a join would have been required.
* [React-color](http://casesandberg.github.io/react-color/) is used for the color picker.
    - Implementing a UX friendly color picker from scratch is a lot of work. It felt out of scope for this project, so I decided to go with a library in order to not reinvent the wheel.
    - If UX input had been provided for the color picker I would have implemented it on a lower level.
* Postgres is used instead of MySQL.
    - A rationale can be found in the Journey section.
* Sass instead of Less.
    - See Journey section for rationale.
* Primitive UI design framework
    - I decided to build upon a framework called [Primitive](https://taniarascia.github.io/primitive/), instead of writing loads of scss myself; reinventing the wheel on how to cater to different browsers
    - This mimics how many companies implement styling. Often theme and most styling decisions are owned by a team of UX experts. Styling done by developers is limited in comparison and deals mostly with layout, padding and similar.
    - I know enough Sass to explain most about everything the Primitive library does. Ask me questions in case you want to test my Sass/CSS abilities.
* Naming of resources: I've interpreted 'Dispatches' as being a higher lever concept than 'Box'. I.e. a dispatch might be something other than a box in the future. For example a letter or a postcard. A dispatch is such an entity in a state where it's ready to send.
    - With that in mind I chose to stick with the name 'dispatch' instead of 'boxes', 'boxList', 'boxTable' or similar. Later, when integrating Redux, I realized that might not have been the best choice of name... dispatch(dispatches) does not ring so well. Might change the name in the future.
* There are some TODO comments in the code that suggest future improvements. Normally I would not commit TODO comments; this is an exception to demonstrate my thoughts and ideas.

# Future work
Several things can be improved in my current solution, and should be before an app like this is ready to be deployed to production. However I had to draw a line somewhere to stop and hand this in.

Below are the things that didn't make it into this initial version. I hope to revisit and build more of that later to continue my learning journey.
* The __No Blue__ bonus.
    - I'm not very entertained by developing algorithical solutions. To solve the problem the hard question to answer is "What is blue?" Almost philisophical. I could not find a clear answer to that.
    - However one idea is to check if blue is the dominant color [b > (r+g)/2]. I.e. the blue color channel is above the mean of the other two.
    - Another possible solution might come from working in the HSL color space, defining a color range for blue Hues and blocking all shades of those.
    - I need to experiment to know what works and not. And discuss what blue is ;)
* Backend validation
    - This is a really important for data consistency and a must before deploying an app to production. I've only implemented frontend valdation in the form. That is great for improving UX but does little to guarantee data consistency. Easily circumvented by the technically inclined, who can send raw requests to the server. There's a comment in the backend code detailing where I indend to implement this.
* Backend caching
    - Even though performance is not in scope for the task it would be nice with a simple caching mechanism towards the database. A list together with a bool variable for invalidation would go a long way. There's a comment in the code detailing where I indend to implement this.
* Improved error handling
    - Backend could send better error messages when exceptions occur, for example due to database connection failure. Currenly a HTML formatted response with error 500 is sent. Not really REST.
    - Frontend UX could be improved by giving better feedback on loading, success and failure of backend communication.
* Switch to calculating totalShipping in backend (or database view) to avoid floating point precision problems. Expose to frontend via dispatches REST endpoint.
* Expose box uuid to use as key in frontend
    - Currently array index is used as key in the Dispatches view. That is not recommended by React because it may impact rendering performance. My array is not changing index all the time so it's okay, but the proper solution would be to use the uuid from the backend instead.
* More Unit tests
    - Frontend: More validation tests for AddBox form
    - Backend: Test controllers
* Integration testing
    - Test REST service and composition/flow.
    - Consider testing database connection.
* E2E testing
    - A few scenarios testing via frontend UI, using real backend and database.
* Slim frontend
    - Move more logic to backend.
    - Let backend provide data constants, such as selectable countries.
* Security considerations if deployed to production
    - Secure communication between containers. HTTPS connection from frontend. [Let's Encrypt](https://letsencrypt.org/).
    - Store secrets (credentials, keys etc) in a secure way.
    - Validation and defensive coding in backend.
    - Restrict docker networking.
* Future proofing
    - Add versioning aspect to REST endpoints.
    - Version database schema.
* Fixups
    - Get rid of SLF4J warnings on backend startup by providing logger dependencies.

# Work breakdown and strategy
## Implementation strategy
I went with a top down implementation strategy.
* Started with developing the frontend in isolation. State handled via React, and later Redux.
* Developed Java backend and integrated with frontend. State stored in memory by the Java app.
* Set up database and integrated backend towards it. State persisted in database.
* Finally I Dockerized the solution.

For the backend I wanted to avoid Java boilerplate code and cargo cult programming as much as possible.
- For example I chose not to use interfaces for everything. Use where necessary. IntelliJ has really good tools for refactoring and extracting interfaces, so there is really no need to have them before they are needed.
- The exception is naming some classes to fit the MVC pattern, in order to demonstrate my understanding.

## Test strategy

* Focus on unit testing model and logic.
* Less focus on composition. That is the domain of integration testing, which is out of scope for this assignment.
* Regarding test doubles
    - Favor fakes/stubs over mocks.
    - Mocks are often not a necessity when focusing on testing model and logic.
    - Mocks are a sign of testing composition, which should be handled by integration tests.

## Work breakdown
A document detailing my work breakdown structure can be provided on request, in case you're interested in seeing how I structured my work.

# My Journey
During this project I turned many stones and built an understanding of the React & Redux ecosystems. Both were new aquaintances but I ended up apreciating both of them. I would definitely choose React over Angular for future projects.

This was my first time ever implementing a REST server on my own. In most other projects I've been adding to the foundation others have already built, following patterns and working with my team. This was a completely different journey; being on my own and facing lots of new decisions. Needed to find out how I myself wanted everything to be set up. When I learn something new I tend go for learning it deeply. It takes a lot longer to get a result, but in the long run it has been a winning strategy for me. I don't want to just do things and make them work. It is important for me to take informed choices; standing able to motivate and explain how the solution works and why.

Initially I wanted to work in a test-first TDD/BDD fashion, but soon had to abandon that approach in favor of experimentation. A test-first strategy is a better fit for solutions where I have lots of prior design experience. Instead  I tried to use experimentation and simple means to implement basic behavior; with the aim of writing tests in conjunction with refactoring and introduction of more advanced technology.

Below are some highlights from my journey. What I've learnt and what I've considered.
- [Thinking in React](https://reactjs.org/docs/thinking-in-react.html) inspired my approach to designing and building the frontend.
- [Tania Rascia](https://www.taniarascia.com/)'s material on both React and Redux has been an invaluable resource for entering the React ecosystem. She manages to find just the right balance and level of detail to aid in quickly grasping advanced concepts. Very impressive.
- React Classes vs Functions and Hooks.
    * I quickly spotted that there was a modernisation trend going on in the React Community, with Hooks being considered the future and classes being left behind as a legacy solution.
    * That made it worth the effort to use functions and hooks as much as possible. For both React and Redux integration.
- I investigated using React Hooks and Context as alternative to Redux. Wanted to answer the question _Is Redux really necessary for developing this application?_
    * My verdict: It's doable with only React, and not that tricky once the core concepts are understood. However with all the bells and whistles of Redux toolkit (time travel debugging etc); Redux is clearly the better alternative, and worth the effort to learn.
- Investigated differences between Sass and Less.
    * I know Sass/scss from my Angular development and wanted to know if Less had something to offer that Sass did not.
    * My conclusion was that Sass is generally considered the better and more powerful alternative. It is somewhat more popular. That was enough reason for me to select Sass over Less, after clearing that it was an okay deviation from the requirements.
- Investigated using BackBone.js for routing and backend communication
    * Since it's part of the Fortnox tech stack I was interested to know how it could fit into my solution.
    * That curiousity made me read through most of [BackBone's extensive getting started gudie](https://backbonejs.org/#Getting-started). It was an interesting read that gave me a grip on the concepts it offers.
    * However in the end I decided to go with React-router, Redux and the native JS fetch api for BE-communication due to it's relative simplicity.
- Investigated React and Redux form libraries
    * Went with a native React/HTML5 implementation instead. Less steep learning curve. Of the libraries I checked https://react-hook-form.com/ stood out as being the most nice and compact alternative.
- Redux-Thunk vs Redux-Saga
    * Wanted to know the differences and similarities. I went with Redux-Thunk as included in Redux Tool Kit.
- Javascript Fetch APIs
    * Looked into Javascript fetch vs fetch-suspense vs axios. Went with native JS fetch for simplicity reasons. Fewer dependencies.
- React Testing Library as alternative to Enzyme
    * Decided to go with RTL instead of Enzyme after reading an eye opening [blog post about testing implementation details](https://kentcdodds.com/blog/testing-implementation-details) by Kent C. Dodds. He happens to be the author of RTL though, so maybe I'm missing something great that Enzyme has to offer ;)
- Jest vs Mocha
    * Went with Jest. Path of least resistance. It was already set up for me with Create React App. I have used some Mocha before though, in other projects. I wanted to know the differences and make an informed choice.
- Java Spring Boot vs more lightweight alternatives
    * My starting point was I wanting to write the backend with as much plain Java as possible. I wanted a solution that I could easily grasp and maintain control over, with as little "magic" as possible. I also wanted to reduce the amount of boilerplate code that Java solutions are infamous for.
    * Quite early on my journey I started looking at Spring Boot... Seemed to be the most popular solution by far. However there was *a lot* of magic to understand, with autowiring and all sorts of annotations sprinkled in the code. Not what I was looking for. Also I got the notion of Spring being rather heavy and resource intensive. What mostly spoke for Spring in this case was the clear MVC structure.
    * My journey led me on to [Spark](http://sparkjava.com/) for implementing my REST service. That felt lightweight enough!
    * Through the Spark documentation I also found [Sql2o](https://www.sql2o.org/). An ORM-free libary for accessing SQL databases, with similar goals as Spark in terms of code style and reducing boilerplate.
    * Those two libraries gave me most of what I needed to implement my backend and I went on my merry way...
    * Stopping only to consider RxJava for composition. However I decided my Java app was not complex enough to merit that dependency.
- Java BigDecimal
    * Switched from double to BigDecimal as soon as I spotted the first precision problems. Much better! Have not used BigDecimal a lot before so I read up on the API.
- CORS
    * I went deeper on how to properly set up CORS on the server. New the basics before but had not touched allowed Headers and Methods.
- Investigated deploying to cloud service
    * I wanted this knowledge as a Plan B option, in case running with docker become problematic for the reviewer.
    * Checked Heroku, AWS, Azure and others. Looked for a free option. Heroku was the most promising but require running DB as add-on instead of Docker container. That has an impact on design. Okay trade-off though.
- MySQL vs Postgres
    * I wanted to know the differences and make an informed choice. [This article](https://www.techrepublic.com/article/mysql-vs-postgresql/) presented a nice comparison.
    * I understand the recommendation to use MySQL due to being a bit faster and less advanced; and that it's commonly used for web applications due to those reasons.
    * However Postgres felt more interesting because of several things. Firstly it's what Fortnox already use, which made it more intersting to try out. Secondly I wanted the possibility to deploy my app to Heroku. Their free tier provide a Postgres database out of the box, whereas MySQL would require using a less well documented add-on. Lastly the Docker image for MySQL came with warnings and disclaimers attached, related to running it on non-Linux platforms. With Postgres and Docker being familiar tools at Fortnox I felt more confident that a Postgres image would work for the reviewer.
    * These factors made me go with Postgres, after consulting with Fortnox if that was an okay choice.
- Backend testing libraries for testing logic.
    * I had a look at several interesting testing libraries but stayed with JUnit 4, together with AssertJ and JUnitParams, since the requirements was specifically unit testing.
    * I investigated newly released JUnit 5 and compared with the more familiar v4. v5 sure has nice features that I want to learn and use, but doing so was not feasable in the scope of this project. Also I was worried that other test depencencies that I wanted to use might not work with v5. Found a [nice tutorial](https://www.vogella.com/tutorials/JUnit/article.html#usingjuni4https://www.vogella.com/tutorials/JUnit/article.html) showing off both versions.
    * I looked into __JBehave__ as a possible fit for the tests. I'm a fan of the __Cucumber__ Gherkin syntax and bringing that to java in an easy way was tempting. Still out of scope though.
- Integration and REST testing. Testing composition.
    * Even though integration testing was out of scope I was interested in finding a nice framework for testing my Spark REST servcie. That was a deeper rabbit hole than I thought. I want to avoid deploying a bulky web server for my testing needs. I looked into Rest-Assured, Jersey Test, Karate Mock Servlet and others.
    * Eventually I made the connection that most of my hits was about testing REST clients and mocking the server. Not testing the real REST service as I wanted. After making that distinction it became easier. All I need is a decent REST client to use in my tests. Apache HttpClient seems promising.
- I also revisited [Approvals testing](http://www.methodsandtools.com/archive/approvaltest.php) which I've been wanting to try since being introduced to it.
    * I would like to use the [Approvals](https://approvaltests.com/) framework, together with a REST client, to test the REST service API. I regard this a future improvement opportunity.
    * On the topic of Approval and Golden Masters testing I found [ReTest](https://retest.de/), a tool that apply the same concept to E2E GUI testing. Very interesting!
- Docker
    * I decided to dockerize the whole solution and use docker-compose to package it nicely together. This was my first serious attempt at using Docker outside a Kata setting.
    * I'm rather proud at what I acomplished but also see room for improvement. There should be a separate docker setup for development work, with code being hot linked (bind mounted) into the containers for faster feedback loops.