import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.util.*;

public class AutoComplete_JFRAME extends JFrame {
    private JTextField prefixField;
    private JTextArea resultArea;

    private class BST {

        private class Node {
            String word;
            Node left, right;

            private Node(String word) {
                this.word = word;
                left = null;
                right = null;
            }
        }

        private Node root;

        private BST() {
            root = null;
        }

        private void insert(String word) {
            root = insertRec(root, word);
        }

        private Node insertRec(Node root, String word) {
            if (root == null) {
                root = new Node(word);
                return root;
            }

            if (word.compareTo(root.word) < 0)
                root.left = insertRec(root.left, word);
            else if (word.compareTo(root.word) > 0)
                root.right = insertRec(root.right, word);

            return root;
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

    public AutoComplete_JFRAME() {
        super("AutoComplete");

        bst = new BST();

        bst.insert("apple");
        bst.insert("application");
        bst.insert("aptitude");
        bst.insert("bat");
        bst.insert("ball");
        bst.insert("banana");
        bst.insert("cat");
        bst.insert("car");
        bst.insert("cartoon");

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout());
        prefixField = new JTextField(20);
        resultArea = new JTextArea(15, 20);
        resultArea.setEditable(false);

        inputPanel.add(new JLabel("Prefix: "));
        inputPanel.add(prefixField);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(mainPanel);

        prefixField.getDocument().addDocumentListener(new DocumentListener() {
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
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateResultArea() {
        resultArea.setText("");
        String prefix = prefixField.getText().trim();
        if (!prefix.isEmpty()) {
            ArrayList<String> words = bst.getWordsStartingWithPrefix(prefix);
            for (String word : words) {
                resultArea.append(word + "\n");
            }
        }
    }

    public static void main(String[] args) {
        new AutoComplete_JFRAME();
    }
}
