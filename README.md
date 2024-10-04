# Software Engineering Methods
- Master Build Status ![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/mtmjnb/sem/main.yml?branch=master)
- Develop Branch Status ![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/mtmjnb/sem/main.yml?branch=develop)
- License [![LICENSE](https://img.shields.io/github/license/mtmjnb/sem.svg?style=flat-square)](https://github.com/mtmjnb/sem/blob/master/LICENSE)
- Release [![Releases](https://img.shields.io/github/release/mtmjnb/sem/all.svg?style=flat-square)](https://github.com/mtmjnb/sem/releases)

## Our Current Process
This is our current workflow.
1. Select an issue to work on.
2. Pull the latest `develop` branch.
3. Start a new feature branch for the issue.
4. Once feature is finished, create JAR file.
5. Update and test Docker configuration with GitHub Actions.
6. Update feature branch with `develop` to ensure feature is up-to-date.
7. Check feature branch still works.
8. Merge feature branch into `develop`.
9. Repeat 1-7 until release is ready.
10. Update the version number in Maven and the Dockerfile.
11. Merge `develop` into `release`.
12. Create a release - including version tag.
13. Merge `release` into `master`.
14. Merge `release` into `develop`.
15. Close the issues.