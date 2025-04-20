import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class TicTacToeGame extends JFrame {
    private static final int SIZE = 20;
    private JButton[][] board = new JButton[SIZE][SIZE];
    private char[][] gameBoard = new char[SIZE][SIZE];
    private String player1Name, player2Name;
    private char currentPlayer = 'X';
    private boolean isAIGame = false;
    private String aiDifficulty = "";
    private JLabel statusLabel;
    private JButton exitButton;
    private Random random = new Random();
    private ArrayList<Player> leaderboard = new ArrayList<>();

    private static class Player {
        String name;
        int score;

        Player(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }

    public TicTacToeGame() {
        showStartScreen();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 149, 237));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

        return button;
    }

    private void showStartScreen() {
        JFrame startFrame = new JFrame("Co caro");
        startFrame.setSize(500, 450);
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setLayout(null);

        JLabel titleLabel = new JLabel("Chon che do", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(0, 50, 500, 50);
        startFrame.add(titleLabel);

        JButton pvpButton = createStyledButton("Nguoi vs Nguoi");
        pvpButton.setBounds(150, 120, 200, 50);
        pvpButton.addActionListener(e -> {
            showPvpNameInput();
            startFrame.dispose();
        });
        startFrame.add(pvpButton);

        JButton pvAIButton = createStyledButton("Danh voi AI");
        pvAIButton.setBounds(150, 190, 200, 50);
        pvAIButton.addActionListener(e -> {
            showPvAINameInput();
            startFrame.dispose();
        });
        startFrame.add(pvAIButton);

        JButton leaderboardButton = createStyledButton("Bang Xep Hang");
        leaderboardButton.setBounds(150, 260, 200, 50);
        leaderboardButton.addActionListener(e -> {
            showLeaderboard();
            startFrame.dispose();
        });
        startFrame.add(leaderboardButton);

        exitButton = createStyledButton("Thoat");
        exitButton.setBounds(150, 330, 200, 50);
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(startFrame, 
                "Ban co chac muon thoat khong?", 
                "Xac nhan thoat", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        startFrame.add(exitButton);

        startFrame.getContentPane().setBackground(new Color(240, 248, 255));
        startFrame.setLocationRelativeTo(null);
        startFrame.setVisible(true);
    }

    private void showLeaderboard() {
        JFrame leaderboardFrame = new JFrame("Bang Xep Hang");
        leaderboardFrame.setSize(400, 500);
        leaderboardFrame.setLayout(new BorderLayout());

        Collections.sort(leaderboard, Comparator.comparingInt((Player p) -> p.score).reversed());

        String[] columnNames = {"Xep hang", "Ten nguoi choi", "Diem"};
        Object[][] data = new Object[leaderboard.size()][3];
        for (int i = 0; i < leaderboard.size(); i++) {
            data[i][0] = i + 1;
            data[i][1] = leaderboard.get(i).name;
            data[i][2] = leaderboard.get(i).score;
        }

        JTable table = new JTable(data, columnNames);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton backButton = createStyledButton("Quay Lai");
        backButton.addActionListener(e -> {
            showStartScreen();
            leaderboardFrame.dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        buttonPanel.setBackground(new Color(240, 248, 255));

        leaderboardFrame.add(scrollPane, BorderLayout.CENTER);
        leaderboardFrame.add(buttonPanel, BorderLayout.SOUTH);
        leaderboardFrame.getContentPane().setBackground(new Color(240, 248, 255));
        leaderboardFrame.setLocationRelativeTo(null);
        leaderboardFrame.setVisible(true);
    }

    private void showPvpNameInput() {
        JFrame nameFrame = new JFrame("Nhap ten nguoi choi");
        nameFrame.setSize(400, 350);
        nameFrame.setLayout(null);

        JLabel label1 = new JLabel("Ten nguoi choi 1:");
        label1.setFont(new Font("Arial", Font.PLAIN, 16));
        label1.setBounds(50, 50, 150, 30);
        JTextField player1Field = new JTextField();
        player1Field.setFont(new Font("Arial", Font.PLAIN, 16));
        player1Field.setBounds(200, 50, 150, 30);

        JLabel label2 = new JLabel("Ten nguoi choi 2:");
        label2.setFont(new Font("Arial", Font.PLAIN, 16));
        label2.setBounds(50, 100, 150, 30);
        JTextField player2Field = new JTextField();
        player2Field.setFont(new Font("Arial", Font.PLAIN, 16));
        player2Field.setBounds(200, 100, 150, 30);

        JButton startButton = createStyledButton("Bat dau");
        startButton.setBounds(50, 160, 130, 40);
        startButton.addActionListener(e -> {
            player1Name = player1Field.getText().isEmpty() ? "Nguoi choi 1" : player1Field.getText();
            player2Name = player2Field.getText().isEmpty() ? "Nguoi choi 2" : player2Field.getText();
            isAIGame = false;
            initializeGame();
            nameFrame.dispose();
        });

        JButton backButton = createStyledButton("Quay Lai");
        backButton.setBounds(200, 160, 130, 40);
        backButton.addActionListener(e -> {
            showStartScreen();
            nameFrame.dispose();
        });

        exitButton = createStyledButton("Thoat");
        exitButton.setBounds(125, 220, 130, 40);
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(nameFrame, 
                "Ban co chac muon thoat khong?", 
                "Xac nhan thoat", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        nameFrame.add(label1);
        nameFrame.add(player1Field);
        nameFrame.add(label2);
        nameFrame.add(player2Field);
        nameFrame.add(startButton);
        nameFrame.add(backButton);
        nameFrame.add(exitButton);

        nameFrame.getContentPane().setBackground(new Color(240, 248, 255));
        nameFrame.setLocationRelativeTo(null);
        nameFrame.setVisible(true);
    }

    private void showPvAINameInput() {
        JFrame aiFrame = new JFrame("Danh voi AI");
        aiFrame.setSize(400, 400);
        aiFrame.setLayout(null);

        JLabel nameLabel = new JLabel("Ten Nguoi Choi:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameLabel.setBounds(50, 50, 150, 30);
        JTextField playerNameField = new JTextField();
        playerNameField.setFont(new Font("Arial", Font.PLAIN, 16));
        playerNameField.setBounds(200, 50, 150, 30);

        JLabel difficultyLabel = new JLabel("Chon Che Do:");
        difficultyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        difficultyLabel.setBounds(50, 100, 150, 30);
        JComboBox<String> difficultyBox = new JComboBox<>(new String[]{"De", "Trung binh", "Kho"});
        difficultyBox.setFont(new Font("Arial", Font.PLAIN, 16));
        difficultyBox.setBounds(200, 100, 150, 30);

        JButton startButton = createStyledButton("Bat Dau");
        startButton.setBounds(50, 160, 130, 40);
        startButton.addActionListener(e -> {
            player1Name = playerNameField.getText().isEmpty() ? "Nguoi Choi" : playerNameField.getText();
            player2Name = "AI";
            aiDifficulty = (String) difficultyBox.getSelectedItem();
            isAIGame = true;
            initializeGame();
            aiFrame.dispose();
        });

        JButton backButton = createStyledButton("Quay lai");
        backButton.setBounds(200, 160, 130, 40);
        backButton.addActionListener(e -> {
            showStartScreen();
            aiFrame.dispose();
        });

        exitButton = createStyledButton("Thoat");
        exitButton.setBounds(125, 220, 130, 40);
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(aiFrame, 
                "Ban co chac muon thoat khong?", 
                "Xac nhan thoat", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        aiFrame.add(nameLabel);
        aiFrame.add(playerNameField);
        aiFrame.add(difficultyLabel);
        aiFrame.add(difficultyBox);
        aiFrame.add(startButton);
        aiFrame.add(backButton);
        aiFrame.add(exitButton);

        aiFrame.getContentPane().setBackground(new Color(240, 248, 255));
        aiFrame.setLocationRelativeTo(null);
        aiFrame.setVisible(true);
    }

    private void initializeGame() {
        board = new JButton[SIZE][SIZE];
        gameBoard = new char[SIZE][SIZE];

        setTitle("Co Caro");
        setSize(1300, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        statusLabel = new JLabel("Luot cua " + (currentPlayer == 'X' ? player1Name : player2Name));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton newGameButton = createStyledButton("Van Moi");
        newGameButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Ban co chac muon lam van moi khong?", 
                "Xac nhan van moi", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                resetGame();
            }
        });

        exitButton = createStyledButton("Thoat");
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Ban co chac muon quay lai man hinh bat dau khong?", 
                "Xac nhan quay lai", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                resetGame();
                ArrayList<Player> savedLeaderboard = new ArrayList<>(leaderboard);
                dispose();
                TicTacToeGame newGame = new TicTacToeGame();
                newGame.leaderboard = savedLeaderboard;
            }
        });

        buttonPanel.add(newGameButton);
        buttonPanel.add(exitButton);
        buttonPanel.setBackground(new Color(240, 248, 255));

        add(statusLabel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = new JButton("");
                board[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                board[i][j].setBackground(Color.WHITE);
                board[i][j].setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                gameBoard[i][j] = ' ';
                int row = i, col = j;
                for (ActionListener al : board[i][j].getActionListeners()) {
                    board[i][j].removeActionListener(al);
                }
                board[i][j].addActionListener(e -> handleMove(row, col));
                boardPanel.add(board[i][j]);
            }
        }

        setLocationRelativeTo(null);
        setVisible(true);

        if (isAIGame && currentPlayer == 'O') {
            makeAIMove();
        }
    }

    private void updateLeaderboard(String winnerName) {
        int pointsToAdd = 1;
        if (isAIGame && winnerName.equals(player1Name)) {
            switch (aiDifficulty) {
                case "De":
                    pointsToAdd = 1;
                    break;
                case "Trung binh":
                    pointsToAdd = 2;
                    break;
                case "Kho":
                    pointsToAdd = 4;
                    break;
            }
        }

        Player player = leaderboard.stream()
            .filter(p -> p.name.equals(winnerName))
            .findFirst()
            .orElse(null);

        if (player == null) {
            leaderboard.add(new Player(winnerName, pointsToAdd));
        } else {
            player.score += pointsToAdd;
        }
    }

    private void handleMove(int row, int col) {
        if (gameBoard[row][col] != ' ' || !board[row][col].isEnabled()) {
            return;
        }

        gameBoard[row][col] = currentPlayer;
        board[row][col].setText(String.valueOf(currentPlayer));
        board[row][col].setForeground(currentPlayer == 'X' ? Color.RED : Color.BLUE);

        if (checkWin(row, col)) {
            String winnerName = currentPlayer == 'X' ? player1Name : player2Name;
            JOptionPane.showMessageDialog(this, winnerName + " Thang!");
            if (!isAIGame || currentPlayer == 'X') {
                updateLeaderboard(winnerName);
            }
            resetGame();
            return;
        }

        if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "Hoa!");
            resetGame();
            return;
        }

        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        statusLabel.setText("Luot cua " + (currentPlayer == 'X' ? player1Name : player2Name));

        if (isAIGame && currentPlayer == 'O') {
            makeAIMove();
        }
    }

    private void makeAIMove() {
        int[] move = null;
        if (aiDifficulty.equals("De")) {
            move = easyAIMove();
        } else if (aiDifficulty.equals("Trung binh")) {
            move = mediumAIMove();
        } else if (aiDifficulty.equals("Kho")) {
            move = hardAIMove();
        }

        if (move != null) {
            handleMove(move[0], move[1]);
        }
    }

    private int[] easyAIMove() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard[i][j] == ' ') {
                    gameBoard[i][j] = 'X';
                    if (checkPotentialWin(i, j, 'X', 4)) {
                        gameBoard[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    gameBoard[i][j] = ' ';
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard[i][j] == ' ') {
                    gameBoard[i][j] = 'O';
                    if (checkPotentialWin(i, j, 'O', 3) || checkPotentialWin(i, j, 'O', 2)) {
                        gameBoard[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    gameBoard[i][j] = ' ';
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard[i][j] == 'X') {
                    for (int di = -1; di <= 1; di++) {
                        for (int dj = -1; dj <= 1; dj++) {
                            int ni = i + di, nj = j + dj;
                            if (ni >= 0 && ni < SIZE && nj >= 0 && nj < SIZE && gameBoard[ni][nj] == ' ') {
                                return new int[]{ni, nj};
                            }
                        }
                    }
                }
            }
        }

        while (true) {
            int i = random.nextInt(SIZE);
            int j = random.nextInt(SIZE);
            if (gameBoard[i][j] == ' ') {
                return new int[]{i, j};
            }
        }
    }

    private int[] mediumAIMove() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard[i][j] == ' ') {
                    gameBoard[i][j] = 'O';
                    if (checkWin(i, j)) {
                        gameBoard[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    gameBoard[i][j] = ' ';
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard[i][j] == ' ') {
                    gameBoard[i][j] = 'X';
                    if (checkWin(i, j)) {
                        gameBoard[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    gameBoard[i][j] = ' ';
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard[i][j] == ' ') {
                    gameBoard[i][j] = 'X';
                    if (checkPotentialWin(i, j, 'X', 3)) {
                        gameBoard[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    gameBoard[i][j] = ' ';
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard[i][j] == ' ') {
                    gameBoard[i][j] = 'O';
                    if (checkPotentialWin(i, j, 'O', 4) || checkPotentialWin(i, j, 'O', 3)) {
                        gameBoard[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    gameBoard[i][j] = ' ';
                }
            }
        }

        return easyAIMove();
    }

    private int[] hardAIMove() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard[i][j] == ' ') {
                    gameBoard[i][j] = 'O';
                    if (checkWin(i, j)) {
                        gameBoard[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    gameBoard[i][j] = ' ';
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard[i][j] == ' ') {
                    gameBoard[i][j] = 'X';
                    if (checkWin(i, j)) {
                        gameBoard[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    gameBoard[i][j] = ' ';
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard[i][j] == ' ') {
                    gameBoard[i][j] = 'O';
                    if (checkPotentialWin(i, j, 'O', 4)) {
                        gameBoard[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    gameBoard[i][j] = ' ';
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard[i][j] == ' ') {
                    gameBoard[i][j] = 'X';
                    if (checkPotentialWin(i, j, 'X', 4)) {
                        gameBoard[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    gameBoard[i][j] = ' ';
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard[i][j] != ' ') {
                    for (int di = -1; di <= 1; di++) {
                        for (int dj = -1; dj <= 1; dj++) {
                            int ni = i + di, nj = j + dj;
                            if (ni >= 0 && ni < SIZE && nj >= 0 && nj < SIZE && gameBoard[ni][nj] == ' ') {
                                gameBoard[ni][nj] = 'O';
                                if (checkPotentialWin(ni, nj, 'O', 3)) {
                                    gameBoard[ni][nj] = ' ';
                                    return new int[]{ni, nj};
                                }
                                gameBoard[ni][nj] = ' ';
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard[i][j] == 'X') {
                    for (int di = -1; di <= 1; di++) {
                        for (int dj = -1; dj <= 1; dj++) {
                            int ni = i + di, nj = j + dj;
                            if (ni >= 0 && ni < SIZE && nj >= 0 && nj < SIZE && gameBoard[ni][nj] == ' ') {
                                return new int[]{ni, nj};
                            }
                        }
                    }
                }
            }
        }

        int center = SIZE / 2;
        for (int i = center - 1; i <= center + 1; i++) {
            for (int j = center - 1; j <= center + 1; j++) {
                if (i >= 0 && i < SIZE && j >= 0 && j < SIZE && gameBoard[i][j] == ' ') {
                    return new int[]{i, j};
                }
            }
        }

        return easyAIMove();
    }

    private boolean checkWin(int row, int col) {
        char player = gameBoard[row][col];
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for (int[] dir : directions) {
            int count = countInDirection(row, col, dir[0], dir[1], player) +
                        countInDirection(row, col, -dir[0], -dir[1], player) - 1;
            if (count >= 5) return true;
        }
        return false;
    }

    private boolean checkPotentialWin(int row, int col, char player, int targetCount) {
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for (int[] dir : directions) {
            int count = countInDirection(row, col, dir[0], dir[1], player) +
                        countInDirection(row, col, -dir[0], -dir[1], player) - 1;
            if (count >= targetCount) return true;
        }
        return false;
    }

    private int countInDirection(int row, int col, int dRow, int dCol, char player) {
        int count = 0;
        int r = row, c = col;
        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && gameBoard[r][c] == player) {
            count++;
            r += dRow;
            c += dCol;
        }
        return count;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (gameBoard[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                gameBoard[i][j] = ' ';
                if (board[i][j] != null) {
                    board[i][j].setText("");
                    board[i][j].setEnabled(true);
                }
            }
        }
        currentPlayer = 'X';
        statusLabel.setText("Luot cua " + player1Name);
        if (isAIGame && currentPlayer == 'O') {
            makeAIMove();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeGame::new);
    }
}