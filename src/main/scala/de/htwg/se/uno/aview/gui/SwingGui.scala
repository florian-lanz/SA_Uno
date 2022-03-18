package de.htwg.se.uno.aview.gui

import de.htwg.se.uno.controller.controllerComponent.{ChooseColor, ControllerInterface, GameChanged, GameEnded, GameNotChanged, GameSizeChanged}

import scala.swing.BorderPanel.Position
import scala.swing._
import scala.swing.event.Key
import scala.swing.Swing.LineBorder
import de.htwg.se.uno.Uno.gui
import javax.swing.JFrame

class SwingGui(controller: ControllerInterface) extends Frame {

  var variableForTests = 0

  listenTo(controller)

  title = "HTWG Uno"

  def gamePanel = new GridPanel(3, 1) {
    if(controller.getNumOfPlayers <= 3) {
      contents += new GridPanel(1, controller.getNumOfPlayers-1) {
        border = LineBorder(java.awt.Color.WHITE, 20)
        background = java.awt.Color.WHITE
        for (i <- 0 to controller.getNumOfPlayers - 2) {
          if (i == 0) {
            if (!controller.nextTurn() && controller.nextEnemy() == 1) {
              contents += new GridPanel(1,1) {
                border = LineBorder(java.awt.Color.GREEN, 1)
                contents += new GridPanel(1, controller.getLength(i)) {
                  border = LineBorder(java.awt.Color.WHITE, 20)
                  background = java.awt.Color.WHITE
                  for (enemy <- 1 to controller.getLength(i)) {
                    val cardPanel = new CardPanel(i, enemy - 1, controller)
                    contents += cardPanel.card
                    listenTo(cardPanel)
                  }
                }
              }
              variableForTests = 1
            } else {
              contents += new GridPanel(1, controller.getLength(i)) {
                border = LineBorder(java.awt.Color.WHITE, 20)
                background = java.awt.Color.WHITE
                for (enemy <- 1 to controller.getLength(i)) {
                  val cardPanel = new CardPanel(i, enemy - 1, controller)
                  contents += cardPanel.card
                  listenTo(cardPanel)
                }
              }
            }
          } else {
            if (!controller.nextTurn() && controller.nextEnemy() == 2) {
              contents += new GridPanel(1,1) {
                border = LineBorder(java.awt.Color.GREEN, 1)
                contents += new GridPanel(1, controller.getLength(i)) {
                  border = LineBorder(java.awt.Color.WHITE, 20)
                  background = java.awt.Color.WHITE
                  for (enemy <- 1 to controller.getLength(i)) {
                    val cardPanel = new CardPanel(i, enemy - 1, controller)
                    contents += cardPanel.card
                    listenTo(cardPanel)
                  }
                }
              }
              variableForTests = 2
            } else {
              contents += new GridPanel(1, controller.getLength(i)) {
                border = LineBorder(java.awt.Color.WHITE, 20)
                background = java.awt.Color.WHITE
                for (enemy <- 1 to controller.getLength(i)) {
                  val cardPanel = new CardPanel(i, enemy - 1, controller)
                  contents += cardPanel.card
                  listenTo(cardPanel)
                }
              }
            }
          }
        }
      }
      contents += new GridPanel(1, 3) {
        border = LineBorder(java.awt.Color.WHITE, 40)
        background = java.awt.Color.WHITE
        for (i <- 0 to 2) {
          val cardPanel = new CardPanel(3, i, controller)
          contents += cardPanel.card
          listenTo(cardPanel)
        }
      }
      contents += new GridPanel(1,1) {
        border = LineBorder(java.awt.Color.WHITE, 20)
        if (controller.nextTurn()) {
          contents += new GridPanel(1, 1) {
            border = LineBorder(java.awt.Color.GREEN, 1)
            contents += new GridPanel(1, controller.getLength(4)) {
              border = LineBorder(java.awt.Color.WHITE, 19)
              background = java.awt.Color.WHITE
              for (player <- 1 to controller.getLength(4)) {
                val cardPanel = new CardPanel(4, player - 1, controller)
                contents += cardPanel.card
                listenTo(cardPanel)
              }
            }
          }
          variableForTests = 3
        } else {
          contents += new GridPanel(1, controller.getLength(4)) {
            border = LineBorder(java.awt.Color.WHITE, 20)
            background = java.awt.Color.WHITE
            for (player <- 1 to controller.getLength(4)) {
              val cardPanel = new CardPanel(4, player - 1, controller)
              contents += cardPanel.card
              listenTo(cardPanel)
            }
          }
        }
      }
    } else {
      contents += new GridPanel(1, controller.getNumOfPlayers-1) {
        border = LineBorder(java.awt.Color.WHITE, 20)
        background = java.awt.Color.WHITE
        for (i <- 0 to 1) {
          if (i == 0) {
            if (!controller.nextTurn() && controller.nextEnemy() == 1) {
              contents += new GridPanel(1,1) {
                border = LineBorder(java.awt.Color.GREEN, 1)
                contents += new GridPanel(1, controller.getLength(i)) {
                  border = LineBorder(java.awt.Color.WHITE, 20)
                  background = java.awt.Color.WHITE
                  for (enemy <- 1 to controller.getLength(i)) {
                    val cardPanel = new CardPanel(i, enemy - 1, controller)
                    contents += cardPanel.card
                    listenTo(cardPanel)
                  }
                }
              }
              variableForTests = 4
            } else {
              contents += new GridPanel(1, controller.getLength(i)) {
                border = LineBorder(java.awt.Color.WHITE, 20)
                background = java.awt.Color.WHITE
                for (enemy <- 1 to controller.getLength(i)) {
                  val cardPanel = new CardPanel(i, enemy - 1, controller)
                  contents += cardPanel.card
                  listenTo(cardPanel)
                }
              }
            }
          } else {
            if (!controller.nextTurn() && controller.nextEnemy() == 2) {
              contents += new GridPanel(1,1) {
                border = LineBorder(java.awt.Color.GREEN, 1)
                contents += new GridPanel(1, controller.getLength(i)) {
                  border = LineBorder(java.awt.Color.WHITE, 20)
                  background = java.awt.Color.WHITE
                  for (enemy <- 1 to controller.getLength(i)) {
                    val cardPanel = new CardPanel(i, enemy - 1, controller)
                    contents += cardPanel.card
                    listenTo(cardPanel)
                  }
                }
              }
              variableForTests = 5
            } else {
              contents += new GridPanel(1, controller.getLength(i)) {
                border = LineBorder(java.awt.Color.WHITE, 20)
                background = java.awt.Color.WHITE
                for (enemy <- 1 to controller.getLength(i)) {
                  val cardPanel = new CardPanel(i, enemy - 1, controller)
                  contents += cardPanel.card
                  listenTo(cardPanel)
                }
              }
            }
          }

        }
      }
      contents += new GridPanel(1, 3) {
        border = LineBorder(java.awt.Color.WHITE, 40)
        background = java.awt.Color.WHITE
        for (i <- 0 to 2) {
          val cardPanel = new CardPanel(3, i, controller)
          contents += cardPanel.card
          listenTo(cardPanel)
        }
      }
      contents += new GridPanel(1, controller.getNumOfPlayers-1) {
        border = LineBorder(java.awt.Color.WHITE, 20)
        background = java.awt.Color.WHITE
        for (i <- 0 to 1) {
          if (i == 1) {
            if (!controller.nextTurn() && controller.nextEnemy() == 3) {
              contents += new GridPanel(1,1) {
                border = LineBorder(java.awt.Color.GREEN, 1)
                contents += new GridPanel(1, controller.getLength(2)) {
                  border = LineBorder(java.awt.Color.WHITE, 20)
                  background = java.awt.Color.WHITE
                  for (enemy <- 1 to controller.getLength(2)) {
                    val cardPanel = new CardPanel(2, enemy - 1, controller)
                    contents += cardPanel.card
                    listenTo(cardPanel)
                  }
                }
              }
              variableForTests = 6
            } else {
              contents += new GridPanel(1, controller.getLength(2)) {
                border = LineBorder(java.awt.Color.WHITE, 20)
                background = java.awt.Color.WHITE
                for (enemy <- 1 to controller.getLength(2)) {
                  val cardPanel = new CardPanel(2, enemy - 1, controller)
                  contents += cardPanel.card
                  listenTo(cardPanel)
                }
              }
            }

          } else {
            if (controller.nextTurn()) {
              contents += new GridPanel(1,1) {
                border = LineBorder(java.awt.Color.GREEN, 1)
                contents += new GridPanel(1, controller.getLength(4)) {
                  border = LineBorder(java.awt.Color.WHITE, 19)
                  background = java.awt.Color.WHITE
                  for (player <- 1 to controller.getLength(4)) {
                    val cardPanel = new CardPanel(4, player - 1, controller)
                    contents += cardPanel.card
                    listenTo(cardPanel)
                  }
                }
              }
              variableForTests = 7
            } else {
              contents += new GridPanel(1, controller.getLength(4)) {
                border = LineBorder(java.awt.Color.WHITE, 19)
                background = java.awt.Color.WHITE
                for (player <- 1 to controller.getLength(4)) {
                  val cardPanel = new CardPanel(4, player - 1, controller)
                  contents += cardPanel.card
                  listenTo(cardPanel)
                }
              }
            }
          }
        }
      }
    }
  }

  var statusline = new TextField(controller.controllerEvent("idle"),30) {
    preferredSize = new Dimension(1,100)
    font = new Font("Verdana", 5, 36)
    background = java.awt.Color.WHITE
  }

  contents = new BorderPanel {
    //add(pushpanel, BorderPanel.Position.North)
    add(gamePanel, Position.Center)
    add(statusline, BorderPanel.Position.South)
  }

  menuBar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.F
      contents += new MenuItem(Action("Test") {controller.createTestGame()})
      contents += new Menu("Random") {
        mnemonic = Key.R
        contents += new MenuItem(Action("2 Players") {controller.createGame(2)})
        contents += new MenuItem(Action("3 Players") {controller.createGame(3)})
        contents += new MenuItem(Action("4 Players") {controller.createGame(4)})
      }
      contents += new MenuItem(Action("Quit") {System.exit(0)})
    }
    contents += new Menu("Edit") {
      mnemonic = Key.E
      contents += new MenuItem(Action("Undo") {controller.undo()})
      contents += new MenuItem(Action("Redo") {controller.redo()})
    }
    contents += new Menu("Game") {
      mnemonic = Key.G
      contents += new MenuItem(Action("Save") {controller.save()})
      contents += new MenuItem(Action("Load") {controller.load()})
    }
  }

  visible = true
  redraw


  reactions += {
    case event: GameSizeChanged => redraw
    case event: GameChanged => redraw
    case event: GameNotChanged => redraw
    case event: GameEnded => ended
    case event: ChooseColor => special
  }

  def redraw = {
    statusline.text = controller.controllerEvent("idle")
    contents = new BorderPanel {
      //add(pushpanel, BorderPanel.Position.North)
      add(gamePanel, BorderPanel.Position.Center)
      add(statusline, BorderPanel.Position.South)
    }
    repaint()
  }

  def special : Unit = {
    statusline.text = controller.controllerEvent("idle")
    contents = new BorderPanel {
      add(pushpanel, BorderPanel.Position.North)
      add(gamePanel, BorderPanel.Position.Center)
      add(statusline, BorderPanel.Position.South)
    }
  }

  def pushpanel = new FlowPanel {
    for (i <- 0 to 3) {
      val button = Button("") {controller.set(controller.getHs2, i + 1)}
      if (i == 0) {
        button.background = new Color(0,0,255)
      } else if (i == 1) {
        button.background = new Color(0,255,0)
      } else if (i == 2) {
        button.background = new Color(255,255,0)
      } else {
        button.background = new Color(255,0,0)
      }
      button.preferredSize_=(new Dimension(90,90))
      contents += button
      listenTo(button)
    }
  }

  def ended: Unit = {
    statusline.text = controller.controllerEvent("idle")
    contents = new BorderPanel {
      add(statusline, BorderPanel.Position.North)
      add(buttons, BorderPanel.Position.Center)
      add(gameEndedLine, BorderPanel.Position.South)
    }
  }

  def buttons = new FlowPanel {
    for (i <- 0 to 4) {
      val button = Button(
        if(i == 0) {
          "New Testgame"
        } else if(i == 1) {
          "Random Game with 2 Players"
        } else if(i == 2) {
          "Random Game with 3 Players"
        } else if (i == 3){
          "Random Game with 4 Players"
        }else {
          "Quit"
        }
      ) {
        if(i == 0) {
          controller.createTestGame()
        } else if(i == 1) {
          controller.createGame(2)
        } else if(i == 2) {
          controller.createGame(3)
        } else if (i == 3){
          controller.createGame(4)
        } else {
          System.exit(0)
        }
      }
      button.preferredSize_=(new Dimension(200,90))
      if(i <= 3) {
        button.background = java.awt.Color.GREEN
      } else {
        button.background = java.awt.Color.RED
      }
      contents += button
      listenTo(button)
    }
    background = java.awt.Color.WHITE
  }

  val gameEndedLine = new TextField("Was möchtest du tun?",30) {
    preferredSize = new Dimension(1,100)
    font = new Font("Verdana", 5, 36)
    background = java.awt.Color.WHITE
    setLocationRelativeTo(buttons)
  }
}
