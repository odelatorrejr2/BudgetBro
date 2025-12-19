# BudgetBro 🎄💸  
**Oscar De La Torre**  
**Class:** CPSC-411A 

---

## App Overview
BudgetBro is a simple personal finance tracker built with **Kotlin** and **Jetpack Compose**.  
Users can sign in, create spending categories, and record income/expense transactions. The app saves data locally using **Room**, and it also includes a dashboard that shows totals and recent activity.

---

## Features
### Authentication (Firebase)
- Sign Up + Login (Email/Password)
- Persistent login (stays logged in after restart)
- Sign out
- Input validation (email format, password length)
- Loading + error messages

### Data Storage (Room Database)
- Stores data locally using Room
- Two related entities:
  - Categories (Food, Gas, Rent, etc.)
  - Transactions (Income/Expense linked to a category)
- Full CRUD (create, read, update, delete)

### Transactions Tools
- Filter: All / Income / Expense
- Search transactions
- Sort transactions (Newest, Amount High→Low, Amount Low→High)

### UI / Navigation
- Jetpack Compose + Material 3
- Navigation between screens
- Confirmation dialog for deletes
- Empty state messages
- Christmas theme styling

---

## Screenshots
- Dashboard:
<img width="1080" height="2400" alt="Screenshot_20251219_020650" src="https://github.com/user-attachments/assets/931aa17f-09a6-497d-8e92-01f68a13e8cd" />

- Login:
<img width="1080" height="2400" alt="Screenshot_20251219_020516" src="https://github.com/user-attachments/assets/f2a94934-deda-4e30-b35e-77a6455717c0" />

- Sign Up:
<img width="1080" height="2400" alt="Screenshot_20251219_020552" src="https://github.com/user-attachments/assets/8a61f45b-a452-4dc6-9709-03ebe950bff2" />

- Categories:
<img width="1080" height="2400" alt="Screenshot_20251219_020702" src="https://github.com/user-attachments/assets/d35532d5-023b-4527-9b2d-daa04c443a80" />

- Transactions:
<img width="1080" height="2400" alt="Screenshot_20251219_020714" src="https://github.com/user-attachments/assets/c31eae66-b21a-4d64-b969-6c7f5acbb600" />

- Add Transaction:
<img width="1080" height="2400" alt="Screenshot_20251219_020733" src="https://github.com/user-attachments/assets/82ec7200-42eb-4633-9378-8689f7886561" />

- Transaction Detail:
<img width="1080" height="2400" alt="Screenshot_20251219_020847" src="https://github.com/user-attachments/assets/9107d0d2-2e75-4ded-a7c1-138d379565db" />

- Category Detail:
<img width="1080" height="2400" alt="Screenshot_20251219_021440" src="https://github.com/user-attachments/assets/42116671-6fb7-4709-9f58-da9525fc2536" />
