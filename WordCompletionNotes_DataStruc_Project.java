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

        // 삭제 메소드
        public void delete(String word) {
            root = deleteNode(root, word);
        }

        private Node deleteNode(Node root, String word) {
            // 루트 노드가 null이면 탐색 종료
            if (root == null) {
                return root;
            }

            // 키가 작으면 왼쪽 서브트리 탐색
            if (word.compareTo(root.word) < 0) {
                root.left = deleteNode(root.left, word);
            }
            // 키가 크면 오른쪽 서브트리 탐색
            else if (word.compareTo(root.word) > 0) {
                root.right = deleteNode(root.right, word);
            }
            // 찾은 노드를 삭제
            else {
                // 자식이 없거나 하나만 있는 경우
                if (root.left == null) {
                    return root.right;
                } else if (root.right == null) {
                    return root.left;
                }

                // 두 자식이 있는 경우, 오른쪽 서브트리에서 최솟값을 찾아옴
                root.word = minValue(root.right);

                // 오른쪽 서브트리에서 최솟값을 가지는 노드를 삭제
                root.right = deleteNode(root.right, root.word);
            }

            return root;
        }

        // 트리에서 가장 작은 값을 찾아 반환하는 메소드
        private String minValue(Node node) {
            String minVal = node.word;
            while (node.left != null) {
                minVal = node.left.word;
                node = node.left;
            }
            return minVal;
        }

        // 모든 BST 노드 삭제
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
        // 단어 저장할 HashMap, BST 클래스 객체 생성
        map = new HashMap<String, wordMean>();
        bst = new BST();

        // mainPanel: 모든 요소를 담는 패널
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout());

        // inputPanel: 입력칸 및 텍스트 패널
        prefixField = new JTextField(20); // 입력 칸
        JLabel inputLabel = new JLabel("Prefix: "); // 입력 칸 옆에 위치하여 실행되는 기능이 출력됨
        inputPanel.add(inputLabel);
        inputPanel.add(prefixField);

        // 단어, 뜻, 중요도 출력 패널을 mainPanel에 추가(각각 왼, 중, 오)
        KeyResultArea = new JTextArea(15, 19); // 단어 textArea
        KeyResultArea.setEditable(false); // setEditable(): textArea 수정 가능 여부(false: 수정 불가)
        ValueResultArea = new JTextArea(15, 15); // 뜻 textArea
        ValueResultArea.setEditable(false);
        LevelResultArea = new JTextArea(15, 19); // 중요도 textArea
        LevelResultArea.setEditable(false);
        mainPanel.add(new JScrollPane(KeyResultArea), BorderLayout.WEST);
        mainPanel.add(new JScrollPane(ValueResultArea), BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(LevelResultArea), BorderLayout.EAST);

        // 각 textArea 설명 라벨
        JLabel wordLabel = new JLabel("단어");
        JLabel meanLabel = new JLabel("뜻");
        JLabel levelLabel = new JLabel("중요도");
        // 설명 라벨을 메뉴 패널에 추가
        JPanel menuPanel = new JPanel(new GridLayout());
        menuPanel.add(wordLabel);
        menuPanel.add(meanLabel);
        menuPanel.add(levelLabel);

        // menuPanel, inputPanel을 topPanel에 추가
        // (flowLayout 패널을 두개 만들어 borderLayout에 추가)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(menuPanel, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 메뉴 버튼 배치 패널
        JPanel btnArea = new JPanel(new FlowLayout());
        btnArea = new JPanel(new FlowLayout());
        mainPanel.add(btnArea, BorderLayout.AFTER_LAST_LINE); // btnArea를 mainPanel의 가장 하단에 배치

        // 하단 메뉴 버튼 생성 및 추가
        JButton btn_showAll = new JButton("전체 보기");
        btnArea.add(btn_showAll, BorderLayout.AFTER_LAST_LINE);
        JButton btn_add = new JButton("단어 추가");
        btnArea.add(btn_add, BorderLayout.AFTER_LAST_LINE);
        JButton btn_remove = new JButton("단어 삭제");
        btnArea.add(btn_remove, BorderLayout.AFTER_LAST_LINE);
        JButton btn_mody = new JButton("단어 수정");
        btnArea.add(btn_mody, BorderLayout.AFTER_LAST_LINE);
        JButton btn_search = new JButton("단어 검색");
        btnArea.add(btn_search, BorderLayout.AFTER_LAST_LINE);
        JButton btn_delAll = new JButton("전부 지우기");
        btnArea.add(btn_delAll, BorderLayout.AFTER_LAST_LINE);
        add(mainPanel);

        // 초기 데이터. Hashmap
        String[][] mapData = {
                { "require", "요구하다", "상" },
                { "tech", "기술", "상" },
                { "regret", "후회하다", "상" },
                { "provide", "제공하다", "상" },
                { "offer", "제공하다", "중" },
                { "heal", "치료하다", "중" },
                { "fashion", "패션", "중" },
                { "game", "게임", "하" },
                { "occur", "발생하다", "하" },
                { "apple", "사과", "하" },
                { "application", "응용", "중" },
                { "aptitude", "적성", "상" },
                { "bat", "박쥐, 방망이", "하" },
                { "ball", "공", "하" },
                { "banana", "바나나", "하" },
                { "cat", "고양이", "하" },
                { "car", "자동차", "하" },
                { "cartoon", "만화", "중" }
        };
        // for each문으로 하나씩 값을 불러와 map.put()으로 저장
        for (String[] row : mapData) {
            wordMean word = new wordMean();
            word.mean = row[1];
            word.level = row[2];
            map.put(row[0], word);
        }

        // 초기 데이터. bst
        // bst노드에 추가할 이중배열
        String bstData[][] = {
                { "apple", "사과", "하" },
                { "application", "응용", "중" },
                { "aptitude", "적성", "상" },
                { "bat", "박쥐, 방망이", "하" },
                { "ball", "공", "하" },
                { "banana", "바나나", "하" },
                { "cat", "고양이", "하" },
                { "car", "자동차", "하" },
                { "cartoon", "만화", "중" },
                { "require", "요구하다", "상" },
                { "tech", "기술", "상" },
                { "regret", "후회하다", "상" },
                { "provide", "제공하다", "상" },
                { "offer", "제공하다", "중" },
                { "heal", "치료하다", "중" },
                { "fashion", "패션", "중" },
                { "game", "게임", "중" },
                { "occur", "발생하다", "하" }
        };
        // for문으로 하나씩 추가
        for (int i = 0; i < bstData.length; i++) {
            bst.insert(bstData[i][0], bstData[i][1], bstData[i][2]);
        }

        // 문서(프레임 창) 리스너
        // 창에서 발생하는 모든 이벤트 읽음
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

        // 전체 보기 버튼 클릭시
        btn_showAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputLabel.setText("전체 보기");
                startSetting();

                prefixField.getDocument().removeDocumentListener(documentListener);
                if (map.isEmpty()) {
                    inputLabel.setText("전체 보기");
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

        // 단어 추가 버튼 클릭시
        btn_add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputLabel.setText("추가할 단어를 입력하세요.");
                startSetting();

                prefixField.getDocument().removeDocumentListener(documentListener);
                prefixField.addKeyListener(new KeyAdapter() {
                    String word;
                    String mean;
                    String level;

                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            if (inputLabel.getText().equals("추가할 단어를 입력하세요.")) {
                                word = prefixField.getText().trim();
                                KeyResultArea.append(word);
                                if (map.containsKey(word)) {
                                    inputLabel.setText("중복된 단어입니다.");
                                } else {
                                    inputLabel.setText("추가할 뜻을 입력하세요.");
                                    prefixField.setText("");
                                }
                            } else if (inputLabel.getText().equals("추가할 뜻을 입력하세요.")) {
                                mean = prefixField.getText().trim();
                                ValueResultArea.append(mean);
                                inputLabel.setText("중요도를 입력하세요.");
                                prefixField.setText("");

                            } else if (inputLabel.getText().equals("중요도를 입력하세요.")) {
                                level = prefixField.getText().trim();
                                LevelResultArea.append(level);
                                inputLabel.setText("추가되었습니다.");
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

        // 단어 삭제 버튼 클릭시
        btn_remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputLabel.setText("삭제할 단어를 입력하세요.");
                startSetting();

                prefixField.getDocument().addDocumentListener(documentListener);
                prefixField.addKeyListener(new KeyAdapter() {
                    String word;

                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            if (inputLabel.getText().equals("삭제할 단어를 입력하세요.")) {
                                word = prefixField.getText().trim();
                                if (map.containsKey(word)) {
                                    map.remove(word);
                                    bst.delete(word);
                                    inputLabel.setText("[" + word + "]를 삭제했습니다.");
                                    prefixField.setText("");
                                } else {
                                    inputLabel.setText("[" + word + "]단어가 없습니다.");
                                    prefixField.setText("");
                                }
                            }
                            prefixField.removeKeyListener(prefixField.getKeyListeners()[0]);
                        }
                    }
                });

            }
        });

        // 단어 수정 버튼 클릭시
        btn_mody.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputLabel.setText("수정할 단어를 입력하세요.");
                startSetting();

                prefixField.getDocument().addDocumentListener(documentListener);
                prefixField.addKeyListener(new KeyAdapter() {
                    String word;
                    String mean;
                    String level;

                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            if (inputLabel.getText().equals("수정할 단어를 입력하세요.")) {
                                word = prefixField.getText().trim();
                                if (map.containsKey(word)) {
                                    map.remove(word);
                                    bst.delete(word);
                                    inputLabel.setText("바꿀 단어를 입력하세요.");
                                    prefixField.setText("");
                                } else {
                                    inputLabel.setText("[" + word + "]단어가 없습니다.");
                                    prefixField.setText("");
                                    return;
                                }
                            } else if (inputLabel.getText().equals("바꿀 단어를 입력하세요.")) {
                                word = prefixField.getText().trim();
                                inputLabel.setText("바꿀 뜻을 입력하세요.");
                                prefixField.setText("");
                            } else if (inputLabel.getText().equals("바꿀 뜻을 입력하세요.")) {
                                mean = prefixField.getText().trim();
                                inputLabel.setText("중요도를 입력하세요.");
                                prefixField.setText("");
                            } else if (inputLabel.getText().equals("중요도를 입력하세요.")) {
                                level = prefixField.getText().trim();
                                inputLabel.setText("추가되었습니다.");
                                prefixField.setText("");

                                wordMean temp = new wordMean();
                                temp.mean = mean;
                                temp.level = level;
                                map.put(word, temp);
                                bst.insert(word, mean, level);
                                inputLabel.setText("[" + word + "]로 수정했습니다.");
                                prefixField.setText("");
                                prefixField.removeKeyListener(prefixField.getKeyListeners()[0]);
                            }
                        }
                    }
                });

            }
        });

        // 전체 삭제 버튼 클릭시
        btn_delAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputLabel.setText("정말로 모두 지우시겠습니까?(y/n)");
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
                                inputLabel.setText("단어 목록을 모두 지웠습니다.");
                                prefixField.setText("");
                            } else if (isDel.equals("n")) {
                                inputLabel.setText("취소하였습니다.");
                                prefixField.setText("");
                            }
                            prefixField.removeKeyListener(prefixField.getKeyListeners()[0]);
                        }
                    }
                });
            }
        });

        // 단어 검색 버튼 클릭시
        btn_search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inputLabel.setText("단어 검색");
                startSetting();
                prefixField.getDocument().addDocumentListener(documentListener);
            }

        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null); // JFrame 창을 윈도우 화면 중앙에 배치(null을 인자로 전달하면 중앙 배치)
        setVisible(true);
        setResizable(false); // 창 크기 조절 막음
    }

    // prefixField 텍스트필드에 텍스트 입력할 때 마다 실시간으로 텍스트 읽음(단어 검색 메소드에서 활용)
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

    // 모든 버튼에 적용. 버튼 클릭 시 단어창에 있는 텍스트들 모두 비우고 키 리스너 제거
    // (제거 안하면 이전에 클릭했던 버튼에서 추가된 키 리스너가 계속 실행)
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
