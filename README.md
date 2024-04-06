[![License](https://img.shields.io/github/license/thinkingrock-gtd/tr-pc)](https://github.com/thinkingrock-gtd/tr-pc/blob/master/LICENSE)
[![Build](https://img.shields.io/github/actions/workflow/status/thinkingrock-gtd/tr-pc/build.yml?branch=main)](https://github.com/thinkingrock-gtd/tr-pc/actions?query=workflow%3AJavaCI+branch%3Amain)

## ThinkingRock for Personal Computer (i.e. any Desktop or Laptop computer)


## License

GNU General Public License v3.0 or later

See [LICENSE](https://github.com/thinkingrock-gtd/tr-pc/blob/master/LICENSE) to see the full text.

## How to build a project
Following is the proven way how to build and run the application from source code.
Please beware, we do not have any prior experiences with Ant and NetBeans.
There may be better ways to launch ThinkingRock.
Any help and experience are highly welcomed.

### Warning
Before starting ThinkingRock from sources, ensure you have a backup of your production data.
We haven't yet figured out the configuration to cleanly separate the dev environment from the production configuration or data.
This includes ReviewActions.xml as well.
This issue will be solved in #33.

### Compilation from a Terminal (without Netbeans)

#### Preconditions
- Java is installed (Java 17)
- [Apache Ant](https://ant.apache.org/manual/install.html) is installed
- Optional: [just](https://github.com/casey/just) is installed

#### Run TR directly from sources
- with 'just' installed, simply run `just run` (after having run `just build` at least once before)

### Build artefacts and run
- open a terminal window (Xterm, Konsole, Dos prompt, PowerShell...) and navigate to the root of the git clone tr-pc
- run `ant build-zip -Dnbplatform.default.netbeans.dest.dir=${path_to_repo}/netbeans-plat/20/ide` (replace `${path_to_repo}` with the absolte path to the checked out `tr-pc` repository)
- you will find a zip file *trgtd.zip* inside the folder *dist*.
  Unzip it, navigate inside into the bin and start the respective binary for your OS.
- with `just` installed, you can simply run `just build-zip`

### Run From NetBeans

#### Precondition

- [Netbeans](https://netbeans.apache.org/download/index.html) is installed
- You have opened the tr-pc project in Netbeans

#### Run
- Open Netbeans and open the project
- Menu File -> Open Project. Navigate to the local git clone of tr-pc.\
![Project](/docs/images/readme_project.png)
- Open the node *Module*, choose one of the TR-Modules (e.g. *TR Calendar*), right-click and select *Open Project*
- Right-click the now open module, and select Run\
![Project run](/docs/images/readme_run.png)
> Note: To our current knowledge, running Ant target run on the main project "Thinking Rock" will not launch the tool. Hence the workaround with running it from e.g. the "TR Calendar" project.

> Note: With Netbeans installed and the project opened once, you don't need to specify the parameter `nbplatform.default.netbeans.dest.dir` when running ant anymore.
