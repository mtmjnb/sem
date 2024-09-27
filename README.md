# Software Engineering Methods
- Master Build Status ![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/mtmjnb/sem/main.yml?branch=master)
- Develop Branch Status ![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/mtmjnb/sem/main.yml?branch=develop)
- License [![LICENSE](https://img.shields.io/github/license/mtmjnb/sem.svg?style=flat-square)](https://github.com/mtmjnb/sem/blob/master/LICENSE)
- Release [![Releases](https://img.shields.io/github/release/mtmjnb/sem/all.svg?style=flat-square)](https://github.com/mtmjnb/sem/releases)

## Our Current Process
This is our current workflow.
1. Pull the latest `develop` branch.
2. Start a new feature branch.
3. Once feature is finished, create JAR file.
4. Update and test Docker configuration with GitHub Actions.
5. Update feature branch with `develop` to ensure feature is up-to-date.
6. Check feature branch still works.
7. Merge feature branch into `develop`.
8. Repeat 2-7 until release is ready.
9. Merge `develop` branch into `release` and create release.
10. Merge `release` into `master` and `develop`.