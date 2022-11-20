import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

        btnSaveNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSaveNewClick(e);
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnUpdateClick(e);
            }
        });

        listPeople.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                listPeopleSelection(e);
            }
        });
        btnAddNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnAddNewClick(e);
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnDeleteClick(e);
            }
        });
    }

    public void btnAddNewClick(ActionEvent e) {
        clearFields();
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    public void btnDeleteClick(ActionEvent e) {
        int personNumber = listPeople.getSelectedIndex();
        if (personNumber >= 0) {
            people.remove(personNumber);
            clearFields();
            refreshPeopleList();
            JOptionPane.showMessageDialog(null,
                    "Contato excluído com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void btnSaveNewClick(ActionEvent e) {
        if (!checkFields()) {
            Person p = new Person(txtName.getText(),txtEmail.getText(),txtTelephone.getText(),txtDateOfBirth.getText());
            people.add(p);
            refreshPeopleList();
            JOptionPane.showMessageDialog(null,
                    "Contato adicionado com sucesso!", "Confimação", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Todos os campos devem ser preenchidos!","Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void btnUpdateClick(ActionEvent e) {
        int personNumber = listPeople.getSelectedIndex();
        if (personNumber >= 0) {
            Person p = people.get(personNumber);
            p.setName(txtName.getText());
            p.setEmail(txtEmail.getText());
            p.setPhoneNumber(txtTelephone.getText());
            p.setDateOfBirth(txtDateOfBirth.getText());
            refreshPeopleList();
            JOptionPane.showMessageDialog(null,
                    "Contato atualizado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void listPeopleSelection(ListSelectionEvent e) {
        int personNumber = listPeople.getSelectedIndex();
        if (personNumber >= 0){
            Person p = people.get(personNumber);
            txtName.setText(p.getName());
            txtEmail.setText(p.getEmail());
            txtTelephone.setText(p.getPhoneNumber());
            txtDateOfBirth.setText(p.getDataOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            labelAge.setText(Integer.toString(p.getAge()) + " anos");
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
            //System.out.println("Adicionando pessoa na lista: " + p.getName());
            listPeopleModel.addElement(p.getName());
        }
    }
    public void addPerson(Person p) {
        people.add(p);
        refreshPeopleList();
    }

    private boolean checkFields() {
        return txtName.getText().equals("") && txtTelephone.getText().equals("") && txtEmail.getText().equals("")
                && txtDateOfBirth.getText().equals("");
    }

    private void clearFields() {
        txtName.setText("");
        txtEmail.setText("");
        txtTelephone.setText("");
        txtDateOfBirth.setText("");
        labelAge.setText("0 anos");
    }

    public static void main(String[] args) {
        Screen screen = new Screen();
        screen.setVisible(true);
        screen.setLocationRelativeTo(null);

        Person p1 = new Person("Marcos Antônio", "marcos@gmail.com", "(33)91111-1111", "13/03/1998");
        Person p2 = new Person("João da Silva", "joao@gmail.com", "(33)92222-2222", "01/03/1995");
        Person p3 = new Person("Suelen Rocha", "suelen@gmail.com", "(33)93333-3333", "01/01/2000");
        Person p4 = new Person("Joyce dos Santos", "joyce@gmail.com", "(33)94444-4444", "06/10/2001");

        screen.addPerson(p1);
        screen.addPerson(p2);
        screen.addPerson(p3);
        screen.addPerson(p4);
    }
}
