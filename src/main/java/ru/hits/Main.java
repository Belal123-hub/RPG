package ru.hits;

import java.util.*;

public class Main {
    static int playerHp = 30;
    static int playerMaxHp = 30;
    static int playerLevel = 1;
    static int playerExp = 0;
    static int playerX = 0;
    static int playerY = 0;
    static List<String> inventory = new ArrayList<>();

    static Random rand = new Random();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Dungeon!");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            if (input.equals("status")) {
                System.out.println("HP: " + playerHp + "/" + playerMaxHp + ", Level: " + playerLevel + ", EXP: " + playerExp);
                System.out.println("Inventory: " + inventory);
            } else if (input.startsWith("go ")) {
                String dir = input.substring(3);
                if (dir.equals("north")) playerY++;
                else if (dir.equals("south")) playerY--;
                else if (dir.equals("east")) playerX++;
                else if (dir.equals("west")) playerX--;
                else System.out.println("Unknown direction.");

                if (rand.nextInt(100) < 50) {
                    System.out.println("A wild monster appears!");
                    fightMonster();
                } else {
                    System.out.println("The room is empty.");
                }
            } else if (input.equals("use potion")) {
                if ((inventory.contains("potion") && playerHp < playerMaxHp) || inventory.contains("medium potion") || inventory.contains("large potion")) {
                    System.out.println("Choose potion type: (1) Small, (2) Medium, (3) Large");
                    String type = scanner.nextLine();
                    int healAmount;
                    if (type.equals("1") && inventory.contains("potion")) {
                        inventory.remove("potion");
                        healAmount = 10;
                    } else if (type.equals("2")) {
                        if (inventory.contains("medium potion") && playerLevel >= 2 && playerHp <= playerMaxHp - 20) {
                            inventory.remove("medium potion");
                            healAmount = 20;
                        } else {
                            System.out.println("You don't have a medium potion.");
                            return;
                        }
                    } else if (type.equals("3")) {
                        if ((inventory.contains("large potion") && playerLevel >= 3 && playerHp <= playerMaxHp - 30) || (playerExp > 50 && inventory.contains("large potion"))) {
                            inventory.remove("large potion");
                            healAmount = 30;
                        } else {
                            System.out.println("You don't have a large potion.");
                            return;
                        }
                    } else {
                        System.out.println("Invalid potion type.");
                        return;
                    }

                    playerHp += healAmount;
                    if (playerHp > playerMaxHp) playerHp = playerMaxHp;
                    System.out.println("You used a potion. HP is now " + playerHp);
                } else {
                    System.out.println("No potions available.");
                }
            } else if (input.equals("search")) {
                System.out.println("You carefully search the room...");
                int event = rand.nextInt(4);
                if (event == 0) {
                    System.out.println("You find a hidden stash with 3 potions!");
                    inventory.add("potion");
                    inventory.add("potion");
                    inventory.add("potion");
                } else if (event == 1) {
                    System.out.println("You trigger a dart trap!");
                    int dmg = rand.nextInt(5) + 1;
                    playerHp -= dmg;
                    System.out.println("You took " + dmg + " damage.");
                } else if (event == 2) {
                    System.out.println("You find nothing.");
                } else {
                    System.out.println("You find a scroll. Use it now? (y/n)");
                    String ans = scanner.nextLine();
                    if (ans.equals("y")) {
                        int effect = rand.nextInt(2);
                        if (effect == 0) {
                            System.out.println("The scroll heals you fully!");
                            playerHp = playerMaxHp;
                        } else {
                            System.out.println("The scroll curses you. You lose 5 HP.");
                            playerHp -= 5;
                        }
                    } else {
                        inventory.add("scroll");
                        System.out.println("Scroll added to inventory.");
                    }
                }
                if (playerHp <= 0) {
                    System.out.println("You died during your search. Game over.");
                    System.exit(0);
                }
            } else if (input.equals("rest")) {
                System.out.println("You decide to rest...");
                if (rand.nextBoolean()) {
                    System.out.println("You rest safely and recover HP.");
                    playerHp += 10;
                    if (playerHp > playerMaxHp) playerHp = playerMaxHp;
                } else {
                    System.out.println("You are attacked in your sleep!");
                    int dmg = rand.nextInt(10) + 5;
                    playerHp -= dmg;
                    System.out.println("You took " + dmg + " damage while sleeping.");
                }
                if (playerHp <= 0) {
                    System.out.println("You died in your sleep. Game over.");
                    System.exit(0);
                }
            } else if (input.equals("exit")) {
                System.out.println("Thanks for playing!");
                break;
            } else {
                System.out.println("Unknown command.");
            }
        }
    }

    static void fightMonster() {
        Scanner scanner = new Scanner(System.in);
        int monsterHp = 20;
        while (monsterHp > 0) {
            System.out.println("Monster HP: " + monsterHp);
            System.out.print("Fight (f) or Run (r)? ");
            String choice = scanner.nextLine();
            if (choice.equals("f")) {
                int dmg = rand.nextInt(6) + 1;
                monsterHp -= dmg;
                System.out.println("You hit the monster for " + dmg + " damage.");
                if (monsterHp > 0) {
                    int mdmg = rand.nextInt(6) + 1;
                    playerHp -= mdmg;
                    System.out.println("Monster hits you for " + mdmg + " damage.");
                    if (playerHp <= 0) {
                        System.out.println("You died! Game over.");
                        System.exit(0);
                    }
                } else {
                    System.out.println("Monster defeated!");
                    playerExp += 10;
                    if (playerExp >= playerLevel * 20) {
                        playerLevel++;
                        playerExp = 0;
                        playerMaxHp += 10;
                        playerHp = playerMaxHp;
                        System.out.println("You leveled up to " + playerLevel + "!");
                    }
                    int potion = rand.nextInt(100);
                    if (potion < 50) {
                        if (potion < 7) {
                            inventory.add("large potion");
                        } else if (potion < 25) {
                            inventory.add("medium potion");
                        } else {
                            inventory.add("potion");
                        }
                        System.out.println("You found a potion!");
                    }
                }
            } else if (choice.equals("r")) {
                System.out.println("You ran away!");
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }
}
