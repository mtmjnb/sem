# Software Engineering Methods
- Master Build Status ![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/mtmjnb/sem/main.yml?branch=master)
- Develop Branch Status ![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/mtmjnb/sem/main.yml?branch=develop)
- License [![LICENSE](https://img.shields.io/github/license/mtmjnb/sem.svg?style=flat-square)](https://github.com/mtmjnb/sem/blob/master/LICENSE)
- Release [![Releases](https://img.shields.io/github/release/mtmjnb/sem/all.svg?style=flat-square)](https://github.com/mtmjnb/sem/releases)

## Our Current Process
This is our current workflow.
1. Decide which user story/stories to work on for the next Sprint.
2. Create a new Sprint on Zube.
3. Add the user story card(s) to the Ready column in Zube.
4. Add any additional task cards to Zube and put in priority order.
5. Pull the latest `develop` branch.
6. Start a new feature branch for the task(s) or user story.
7. Select task to work on in Zube.
8. Work on task.
9. Repeat 7-8 until feature is complete.
10. Once feature is finished, create JAR file.
11. Update and test Docker configuration with GitHub Actions.
12. Update feature branch with `develop` to ensure feature is up-to-date.
13. Check feature branch still works.
14. Merge feature branch into `develop`.
15. Repeat 5-14 until Sprint is complete.
16. Merge `develop` branch into `release` and create release.
17. Merge `release` into `master` and `develop`.
18. Close the Sprint.