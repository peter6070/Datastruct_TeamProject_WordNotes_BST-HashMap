import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

class wordMean {
    String mean;
    String level;
}

public class WordCompletionNotes_DataStruc_Project extends JFrame {
    private JTextField prefixField;
    private JTextArea KeyResultArea;
    private JTextArea ValueResultArea;
    private JTextArea LevelResultArea;
    boolean isClickedAdd = false;
    private HashMap<String, wordMean> map;

    private class BST {
        private class Node {
            String word;
            String mean;
            String level;
            Node left, right;

            private Node(String word, String mean, String level) {
                this.word = word;
                this.mean = mean;
                this.level = level;
                left = null;
                right = null;
            }
        }

        private Node root;

        private BST() {
            root = null;
        }

        private void insert(String word, String mean, String level) {
            root = insertRec(root, word, mean, level);
        }

        private Node insertRec(Node root, String word, String mean, String level) {
            if (root == null) {
                root = new Node(word, mean, level);
                return root;
            }
            if (word == null || mean == null || level == null) {
                return root;
            }
            if (word.compareTo(root.word) < 0)
                root.left = insertRec(root.left, word, mean, level);
            else if (word.compareTo(root.word) > 0)
                root.right = insertRec(root.right, word, mean, level);

            return root;
        }

        // ���� �޼ҵ�
        public void delete(String word) {
            root = deleteNode(root, word);
        }

        private Node deleteNode(Node root, String word) {
            // ��Ʈ ��尡 null�̸� Ž�� ����
            if (root == null) {
                return root;
            }

            // Ű�� ������ ���� ����Ʈ�� Ž��
            if (word.compareTo(root.word) < 0) {
                root.left = deleteNode(root.left, word);
            }
            // Ű�� ũ�� ������ ����Ʈ�� Ž��
            else if (word.compareTo(root.word) > 0) {
                root.right = deleteNode(root.right, word);
            }
            // ã�� ��带 ����
            else {
                // �ڽ��� ���ų� �ϳ��� �ִ� ���
                if (root.left == null) {
                    return root.right;
                } else if (root.right == null) {
                    return root.left;
                }

                // �� �ڽ��� �ִ� ���, ������ ����Ʈ������ �ּڰ��� ã�ƿ�
                root.word = minValue(root.right);

                // ������ ����Ʈ������ �ּڰ��� ������ ��带 ����
                root.right = deleteNode(root.right, root.word);
            }

            return root;
        }

        // Ʈ������ ���� ���� ���� ã�� ��ȯ�ϴ� �޼ҵ�
        private String minValue(Node node) {
            String minVal = node.word;
            while (node.left != null) {
                minVal = node.left.word;
                node = node.left;
            }
            return minVal;
        }

        // ��� BST ��� ����
        public void delAll() {
            root = delAll(root);
        }

        private Node delAll(Node root) {
            if (root == null) {
                return null;
            }

            root.left = delAll(root.left);
            root.right = delAll(root.right);

            return null;
        }

        private ArrayList<String> getWordsStartingWithPrefix(String prefix) {
            ArrayList<String> result = new ArrayList<>();
            getWordsStartingWithPrefixRec(root, prefix, result);
            return result;
        }

        private void getWordsStartingWithPrefixRec(Node root, String prefix, ArrayList<String> result) {
            if (root == null)
                return;

            if (root.word.startsWith(prefix))
                result.add(root.word);

            if (prefix.compareTo(root.word) < 0)
                getWordsStartingWithPrefixRec(root.left, prefix, result);
            getWordsStartingWithPrefixRec(root.right, prefix, result);
        }
    }

    private BST bst;

    public WordCompletionNotes_DataStruc_Project() {
        super("AutoCompleteNotes");
        // �ܾ� ������ HashMap, BST Ŭ���� ��ü ����
        map = new HashMap<String, wordMean>();
        bst = new BST();

        // mainPanel: ��� ��Ҹ� ��� �г�
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout());

        // inputPanel: �Է�ĭ �� �ؽ�Ʈ �г�
        prefixField = new JTextField(20); // �Է� ĭ
        JLabel inputLabel = new JLabel("Prefix: "); // �Է� ĭ ���� ��ġ�Ͽ� ����Ǵ� ����� ��µ�
        inputPanel.add(inputLabel);
        inputPanel.add(prefixField);

        // �ܾ�, ��, �߿䵵 ��� �г��� mainPanel�� �߰�(���� ��, ��, ��)
        KeyResultArea = new JTextArea(15, 19); // �ܾ� textArea
        KeyResultArea.setEditable(false); // setEditable(): textArea ���� ���� ����(false: ���� �Ұ�)
        ValueResultArea = new JTextArea(15, 15); // �� textArea
        ValueResultArea.setEditable(false);
        LevelResultArea = new JTextArea(15, 19); // �߿䵵 textArea
        LevelResultArea.setEditable(false);
        mainPanel.add(new JScrollPane(KeyResultArea), BorderLayout.WEST);
        mainPanel.add(new JScrollPane(ValueResultArea), BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(LevelResultArea), BorderLayout.EAST);

        // �� textArea ���� ��
        JLabel wordLabel = new JLabel("�ܾ�");
        JLabel meanLabel = new JLabel("��");
        JLabel levelLabel = new JLabel("�߿䵵");
        // ���� ���� �޴� �гο� �߰�
        JPanel menuPanel = new JPanel(new GridLayout());
        menuPanel.add(wordLabel);
        menuPanel.add(meanLabel);
        menuPanel.add(levelLabel);

        // menuPanel, inputPanel�� topPanel�� �߰�
        // (flowLayout �г��� �ΰ� ����� borderLayout�� �߰�)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(menuPanel, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // �޴� ��ư ��ġ �г�
        JPanel btnArea = new JPanel(new FlowLayout());
        btnArea = new JPanel(new FlowLayout());
        mainPanel.add(btnArea, BorderLayout.AFTER_LAST_LINE); // btnArea�� mainPanel�� ���� �ϴܿ� ��ġ

        // �ϴ� �޴� ��ư ���� �� �߰�
        JButton btn_showAll = new JButton("��ü ����");
        btnArea.add(btn_showAll, BorderLayout.AFTER_LAST_LINE);
        JButton btn_add = new JButton("�ܾ� �߰�");
        btnArea.add(btn_add, BorderLayout.AFTER_LAST_LINE);
        JButton btn_remove = new JButton("�ܾ� ����");
        btnArea.add(btn_remove, BorderLayout.AFTER_LAST_LINE);
        JButton btn_mody = new JButton("�ܾ� ����");
        btnArea.add(btn_mody, BorderLayout.AFTER_LAST_LINE);
        JButton btn_search = new JButton("�ܾ� �˻�");
        btnArea.add(btn_search, BorderLayout.AFTER_LAST_LINE);
        JButton btn_delAll = new JButton("���� �����");
        btnArea.add(btn_delAll, BorderLayout.AFTER_LAST_LINE);
        add(mainPanel);

        // �ʱ� ������. Hashmap
        String[][] mapData = {
                { "require", "�䱸�ϴ�", "��" },
                { "tech", "���", "��" },
                { "regret", "��ȸ�ϴ�", "��" },
                { "provide", "�����ϴ�", "��" },
                { "offer", "�����ϴ�", "��" },
                { "heal", "ġ���ϴ�", "��" },
                { "fashion", "�м�", "��" },
                { "game", "����", "��" },
                { "occur", "�߻��ϴ�", "��" },
                { "apple", "���", "��" },
                { "application", "����", "��" },
                { "aptitude", "����", "��" },
                { "bat", "����, �����", "��" },
                { "ball", "��", "��" },
                { "banana", "�ٳ���", "��" },
                { "cat", "�����", "��" },
                { "car", "�ڵ���", "��" },
                { "cartoon", "��ȭ", "��" }
        };
        // for each������ �ϳ��� ���� �ҷ��� map.put()���� ����
        for (String[] row : mapData) {
            wordMean word = new wordMean();
            word.mean = row[1];
            word.level = row[2];
            map.put(row[0], word);
        }

        // �ʱ� ������. bst
        // bst��忡 �߰��� ���߹迭
        String bstData[][] = {
                { "apple", "���", "��" },
                { "application", "����", "��" },
                { "aptitude", "����", "��" },
                { "bat", "����, �����", "��" },
                { "ball", "��", "��" },
                { "banana", "�ٳ���", "��" },
                { "cat", "�����", "��" },
                { "car", "�ڵ���", "��" },
                { "cartoon", "��ȭ", "��" },
                { "require", "�䱸�ϴ�", "��" },
                { "tech", "���", "��" },
                { "regret", "��ȸ�ϴ�", "��" },
                { "provide", "�����ϴ�", "��" },
                { "offer", "�����ϴ�", "��" },
                { "heal", "ġ���ϴ�", "��" },
                { "fashion", "�м�", "��" },
                { "game", "����", "��" },
                { "occur", "�߻��ϴ�", "��" }
        };
        // for������ �ϳ��� �߰�
        for (int i = 0; i < bstData.length; i++) {
            bst.insert(bstData[i][0], bstData[i][1], bstData[i][2]);
        }

        // ����(������ â) ������
        // â���� �߻��ϴ� ��� �̺�Ʈ ����
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateResultArea();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateResultArea();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateResultArea();
            }
        };

        // ��ü ���� ��ư Ŭ����
        btn_showAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputLabel.setText("��ü ����");
                startSetting();

                prefixField.getDocument().removeDocumentListener(documentListener);
                if (map.isEmpty()) {
                    inputLabel.setText("��ü ����");
                    prefixField.setText("");
                } else {
                    Iterator<Entry<String, wordMean>> entries = map.entrySet().iterator();

                    while (entries.hasNext()) {
                        Map.Entry<String, wordMean> entry = entries.next();
                        KeyResultArea.append(entry.getKey() + "\n");
                        ValueResultArea.append(entry.getValue().mean + "\n");
                        LevelResultArea.append(entry.getValue().level + "\n");
                    }
                }
            }
        });

        // �ܾ� �߰� ��ư Ŭ����
        btn_add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputLabel.setText("�߰��� �ܾ �Է��ϼ���.");
                startSetting();

                prefixField.getDocument().removeDocumentListener(documentListener);
                prefixField.addKeyListener(new KeyAdapter() {
                    String word;
                    String mean;
                    String level;

                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            if (inputLabel.getText().equals("�߰��� �ܾ �Է��ϼ���.")) {
                                word = prefixField.getText().trim();
                                KeyResultArea.append(word);
                                if (map.containsKey(word)) {
                                    inputLabel.setText("�ߺ��� �ܾ��Դϴ�.");
                                } else {
                                    inputLabel.setText("�߰��� ���� �Է��ϼ���.");
                                    prefixField.setText("");
                                }
                            } else if (inputLabel.getText().equals("�߰��� ���� �Է��ϼ���.")) {
                                mean = prefixField.getText().trim();
                                ValueResultArea.append(mean);
                                inputLabel.setText("�߿䵵�� �Է��ϼ���.");
                                prefixField.setText("");

                            } else if (inputLabel.getText().equals("�߿䵵�� �Է��ϼ���.")) {
                                level = prefixField.getText().trim();
                                LevelResultArea.append(level);
                                inputLabel.setText("�߰��Ǿ����ϴ�.");
                                prefixField.setText("");

                                wordMean temp = new wordMean();
                                temp.mean = mean;
                                temp.level = level;
                                map.put(word, temp);
                                bst.insert(word, mean, level);
                                prefixField.removeKeyListener(prefixField.getKeyListeners()[0]);
                            }
                        }
                    }
                });

            }
        });

        // �ܾ� ���� ��ư Ŭ����
        btn_remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputLabel.setText("������ �ܾ �Է��ϼ���.");
                startSetting();

                prefixField.getDocument().addDocumentListener(documentListener);
                prefixField.addKeyListener(new KeyAdapter() {
                    String word;

                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            if (inputLabel.getText().equals("������ �ܾ �Է��ϼ���.")) {
                                word = prefixField.getText().trim();
                                if (map.containsKey(word)) {
                                    map.remove(word);
                                    bst.delete(word);
                                    inputLabel.setText("[" + word + "]�� �����߽��ϴ�.");
                                    prefixField.setText("");
                                } else {
                                    inputLabel.setText("[" + word + "]�ܾ �����ϴ�.");
                                    prefixField.setText("");
                                }
                            }
                            prefixField.removeKeyListener(prefixField.getKeyListeners()[0]);
                        }
                    }
                });

            }
        });

        // �ܾ� ���� ��ư Ŭ����
        btn_mody.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputLabel.setText("������ �ܾ �Է��ϼ���.");
                startSetting();

                prefixField.getDocument().addDocumentListener(documentListener);
                prefixField.addKeyListener(new KeyAdapter() {
                    String word;
                    String mean;
                    String level;

                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            if (inputLabel.getText().equals("������ �ܾ �Է��ϼ���.")) {
                                word = prefixField.getText().trim();
                                if (map.containsKey(word)) {
                                    map.remove(word);
                                    bst.delete(word);
                                    inputLabel.setText("�ٲ� �ܾ �Է��ϼ���.");
                                    prefixField.setText("");
                                } else {
                                    inputLabel.setText("[" + word + "]�ܾ �����ϴ�.");
                                    prefixField.setText("");
                                    return;
                                }
                            } else if (inputLabel.getText().equals("�ٲ� �ܾ �Է��ϼ���.")) {
                                word = prefixField.getText().trim();
                                inputLabel.setText("�ٲ� ���� �Է��ϼ���.");
                                prefixField.setText("");
                            } else if (inputLabel.getText().equals("�ٲ� ���� �Է��ϼ���.")) {
                                mean = prefixField.getText().trim();
                                inputLabel.setText("�߿䵵�� �Է��ϼ���.");
                                prefixField.setText("");
                            } else if (inputLabel.getText().equals("�߿䵵�� �Է��ϼ���.")) {
                                level = prefixField.getText().trim();
                                inputLabel.setText("�߰��Ǿ����ϴ�.");
                                prefixField.setText("");

                                wordMean temp = new wordMean();
                                temp.mean = mean;
                                temp.level = level;
                                map.put(word, temp);
                                bst.insert(word, mean, level);
                                inputLabel.setText("[" + word + "]�� �����߽��ϴ�.");
                                prefixField.setText("");
                                prefixField.removeKeyListener(prefixField.getKeyListeners()[0]);
                            }
                        }
                    }
                });

            }
        });

        // ��ü ���� ��ư Ŭ����
        btn_delAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputLabel.setText("������ ��� ����ðڽ��ϱ�?(y/n)");
                startSetting();

                prefixField.getDocument().removeDocumentListener(documentListener);
                prefixField.addKeyListener(new KeyAdapter() {
                    String isDel;

                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            isDel = prefixField.getText().trim();
                            if (isDel.equals("y")) {
                                bst.delAll();
                                map.clear();
                                inputLabel.setText("�ܾ� ����� ��� �������ϴ�.");
                                prefixField.setText("");
                            } else if (isDel.equals("n")) {
                                inputLabel.setText("����Ͽ����ϴ�.");
                                prefixField.setText("");
                            }
                            prefixField.removeKeyListener(prefixField.getKeyListeners()[0]);
                        }
                    }
                });
            }
        });

        // �ܾ� �˻� ��ư Ŭ����
        btn_search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputLabel.setText("�ܾ� �˻�");
                startSetting();
                prefixField.getDocument().addDocumentListener(documentListener);
            }

        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null); // JFrame â�� ������ ȭ�� �߾ӿ� ��ġ(null�� ���ڷ� �����ϸ� �߾� ��ġ)
        setVisible(true);
        setResizable(false); // â ũ�� ���� ����
    }

    // prefixField �ؽ�Ʈ�ʵ忡 �ؽ�Ʈ �Է��� �� ���� �ǽð����� �ؽ�Ʈ ����(�ܾ� �˻� �޼ҵ忡�� Ȱ��)
    private void updateResultArea() {
        KeyResultArea.setText("");
        ValueResultArea.setText("");
        LevelResultArea.setText("");
        String prefix = prefixField.getText().trim();
        if (!prefix.isEmpty()) {
            ArrayList<String> words = bst.getWordsStartingWithPrefix(prefix);
            for (String word : words) {
                KeyResultArea.append(word + "\n");
                wordMean temp = map.get(word);
                if (temp != null) {
                    ValueResultArea.append(temp.mean + "\n");
                    LevelResultArea.append(temp.level + "\n");
                }
            }
        }
    }

    // ��� ��ư�� ����. ��ư Ŭ�� �� �ܾ�â�� �ִ� �ؽ�Ʈ�� ��� ���� Ű ������ ����
    // (���� ���ϸ� ������ Ŭ���ߴ� ��ư���� �߰��� Ű �����ʰ� ��� ����)
    private void startSetting() {
        prefixField.requestFocus();
        KeyResultArea.setText("");
        ValueResultArea.setText("");
        LevelResultArea.setText("");
        prefixField.setText("");
        if (prefixField.getKeyListeners().length > 0)
            prefixField.removeKeyListener(prefixField.getKeyListeners()[0]);
    }

    public static void main(String[] args) {
        new WordCompletionNotes_DataStruc_Project();
    }
}
