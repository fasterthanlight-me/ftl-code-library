# How to setup CircleCI

**Continuous integration** is a practice that encourages developers to integrate their code into a `main` branch of a shared repository early and often. Instead of building out features in isolation and integrating them at the end of a development cycle, code is integrated with the shared repository by each developer multiple times throughout the day.

Continuous integration is a key step to digital transformation.

Every developer commits daily to a shared mainline. Every commit triggers automated tests and builds. If these fail, they can be repaired quickly - within minutes.

More here: [https://circleci.com/docs/about-circleci](https://circleci.com/docs/about-circleci)


Before working with Circle CI you should read about it’s [concepts](https://circleci.com/docs/concepts)

**Orbs**

Orbs are reusable snippets of code that help automate repeated processes, accelerate project setup, and make it easy to integrate with third-party tools.

More here: [https://circleci.com/docs/orb-intro](https://circleci.com/docs/orb-intro)

Circle CI images

For convenience, CircleCI maintains several Docker images. These images are typically extensions of official Docker images, and include tools especially useful for CI/CD.

More here: [https://circleci.com/docs/circleci-images](https://circleci.com/docs/circleci-images)

## Steps to install Circle CI

1. Create account in [Circle CI](https://circleci.com/)
2. Connect with your Github Account
3. Add Project to Circle CI (it will show you repositories from Github)
- Operating System: Linux
- Language: Node
4. Follow the steps which are descibed in Project Install flow
5. as a result you should have `.circleci/config.yml` with sample config

or you can follow steps from this [guide](https://circleci.com/docs/getting-started)

## How to add ENV keys

To add private keys or secret environment variables for use throughout your project, use the [Environment variables page](https://circleci.com/docs/set-environment-variable#set-an-environment-variable-in-a-project) under Project Settings on the CircleCI web app. 

## Basic example of Yarn package manager caching

Yarn is an open-source package manager for JavaScript. The packages it installs can be cached, which can speed up builds, but, more importantly, can reduce errors related to network connectivity.

Please note, the release of Yarn 2.x comes with a the ability to do Zero Installs. If you are using Zero Installs, you should not need to do any special caching within CircleCI.
You can read more about this & check examples - [here](https://circleci.com/docs/caching#basic-example-of-yarn-package-manager-caching)