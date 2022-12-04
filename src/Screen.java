import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import org.json.*;


public class Screen extends JFrame {
    private JPanel panelTop;
    private JPanel panelLeft;
    private JPanel panelRight;
    private JList<String> listPeople;
    private JButton btnSaveNew;
    private JButton btnUpdate;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtTelephone;
    private JLabel labelAge;
    private JTextField txtDateOfBirth;
    private JPanel panelMain;
    private JButton btnAddNew;
    private JButton btnDelete;
    private ArrayList<Person> people;
    private DefaultListModel<String> listPeopleModel;

    Screen() {
        super("Meus Contatos");
        this.setContentPane(this.panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        people = new ArrayList<>();
        listPeopleModel = new DefaultListModel<>();
        listPeople.setModel(listPeopleModel);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSaveNew.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSaveNew.addActionListener(e -> btnSaveNewClick());

        btnUpdate.addActionListener(e -> btnUpdateClick());

        listPeople.addListSelectionListener(e -> listPeopleSelection());

        btnAddNew.addActionListener(e -> btnAddNewClick());

        btnDelete.addActionListener(e -> btnDeleteClick());
    }

    public void btnAddNewClick() {
        clearFields();
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    public void btnDeleteClick() {
        int personNumber = listPeople.getSelectedIndex();
        int option = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir este contato?",
                "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (personNumber >= 0 && option == 0) {
            people.remove(personNumber);
            clearFields();
            refreshPeopleList();
            saveData();
            JOptionPane.showMessageDialog(null,
                    "Contato excluído com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void btnSaveNewClick() {
        if (!checkFields()) {
            Person p = new Person(txtName.getText(),txtEmail.getText(),txtTelephone.getText(),txtDateOfBirth.getText());
            people.add(p);
            if (saveData()) {
                refreshPeopleList();
                JOptionPane.showMessageDialog(null,
                        "Contato adicionado com sucesso!", "Confimação", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Todos os campos devem ser preenchidos!","Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void btnUpdateClick() {
        int personNumber = listPeople.getSelectedIndex();
        if (personNumber >= 0) {
            Person p = people.get(personNumber);
            p.setName(txtName.getText());
            p.setEmail(txtEmail.getText());
            p.setPhoneNumber(txtTelephone.getText());
            p.setDateOfBirth(txtDateOfBirth.getText());
            refreshPeopleList();
            saveData();
            JOptionPane.showMessageDialog(null,
                    "Contato atualizado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void listPeopleSelection() {
        int personNumber = listPeople.getSelectedIndex();
        if (personNumber >= 0){
            Person p = people.get(personNumber);
            txtName.setText(p.getName());
            txtEmail.setText(p.getEmail());
            txtTelephone.setText(p.getPhoneNumber());
            txtDateOfBirth.setText(p.getDateOfBirthString());
            labelAge.setText(p.getAge() + " anos");
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
        } else {
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
        }
    }
    public void refreshPeopleList() {
        listPeopleModel.removeAllElements();
        for (Person p: people) {
            listPeopleModel.addElement(p.getName());
        }
    }
    public void addPerson(Person p) {
        people.add(p);
        refreshPeopleList();
    }

    private boolean checkFields() {
        return txtName.getText().equals("") || txtTelephone.getText().equals("") || txtEmail.getText().equals("")
                || txtDateOfBirth.getText().equals("");
    }

    private void clearFields() {
        txtName.setText("");
        txtEmail.setText("");
        txtTelephone.setText("");
        txtDateOfBirth.setText("");
        labelAge.setText("0 anos");
    }

    private boolean saveData() {
        JSONArray jsonArray = new JSONArray();
        for (Person person : people) {
            JSONObject jsonObject = new JSONObject(person);
            jsonArray.put(jsonObject);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Contatos.txt"))) {
            bw.write(jsonArray.toString());
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar o contato",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private ArrayList<Person> loadData() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Person> results = new ArrayList<>();

        try (Scanner reader = new Scanner(new FileReader("Contatos.txt"))) {
            while (reader.hasNextLine()) {
                sb.append(reader.nextLine());
            }

            JSONArray jsonArray = new JSONArray(sb.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Person p = new Person(
                        jsonObject.getString("name"),
                        jsonObject.getString("email"),
                        jsonObject.getString("phoneNumber"),
                        jsonObject.getString("dateOfBirthString")
                );
                results.add(p);
            }
            return results;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível carregar os contatos!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static void main(String[] args) {
        Screen screen = new Screen();
        screen.setVisible(true);
        screen.setLocationRelativeTo(null);

        for (Person p: Objects.requireNonNull(screen.loadData())) {
            screen.addPerson(p);
        }

        /*Person p1 = new Person("Marcos Antônio", "marcos@gmail.com", "(33)91111-1111", "13/03/1998");
        Person p2 = new Person("João da Silva", "joao@gmail.com", "(33)92222-2222", "01/03/1995");
        Person p3 = new Person("Suelen Rocha", "suelen@gmail.com", "(33)93333-3333", "01/01/2000");
        Person p4 = new Person("Joyce dos Santos", "joyce@gmail.com", "(33)94444-4444", "06/10/2001");

        screen.addPerson(p1);
        screen.addPerson(p2);
        screen.addPerson(p3);
        screen.addPerson(p4);*/
    }
}
