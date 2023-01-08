[![License](https://img.shields.io/github/license/thinkingrock-gtd/tr-pc)](https://github.com/thinkingrock-gtd/tr-pc/blob/master/LICENSE)

## ThinkingRock for Personal Computer (i.e. any Desktop or Laptop computer)


## License

GNU General Public License v3.0 or later

See [LICENSE](https://github.com/thinkingrock-gtd/tr-pc/blob/master/LICENSE) to see the full text.

## How to build a project
Following is the proven way how to build and run the application from source code.
Please beware, we do not have any prior experiences with Ant and NetBeans.
There may be better ways to launch ThinkinRock.
Any help and experience are highly welcomed.

### Warning
Before starting ThinkinRock from sources, ensure you have a backup of your production data.
We haven't yet figured out the configuration to cleanly separate the dev environment from the production configuration or data.
This includes ReviewActions.xml as well.
This issue will be solved in #33.

### Preconditions
- Java is installed
- [Netbeans](https://netbeans.apache.org/download/index.html) is installed
- [Apache Ant](https://ant.apache.org/manual/install.html) is installed
- You have opened the tr-pc project in Netbeans

### Compilation from a Terminal
- open a terminal window (Xterm, Konsole, Dos prompt, PowerShell...) and navigate to the root of the git clone tr-pc
- run `ant build-zip`
- you will find a zip file *trgtd.zip* inside the folder *dist*.
  Unzip it, navigate inside into the bin and start the respective binary for your OS.
> Note: It seems to be necessary to have opened the project with Netbeans once. Netbeans installs some dependencies that are also used if to build the project using ant only.

### Run From NetBeans
- Menu File -> Open Project. Navigate to the local git clone of tr-pc.

![Project](/docs/images/readme_project.png)
- Open the node *Module*, choose one of the TR-Modules (e.g. *TR Calendar*), right-click and select *Open Project*
- Right-click the now open module, and select Run

![Project run](/docs/images/readme_run.png)
> Note: To our current knowledge, running Ant target run on the main project "Thinking Rock" will not launch the tool. Hence the workaround with running it from e.g. the "TR Calendar" project.
