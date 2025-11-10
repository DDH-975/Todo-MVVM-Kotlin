# Todo-MVVM-Kotlin

### 프로젝트 설명

이 프로젝트는 **MVVM 패턴 실습**에 초점을 맞춘 간단한 TodoList 앱입니다.
Room 데이터베이스를 활용하여 로컬에 데이터를 저장하며, View ↔ ViewModel ↔ Model 간의 역할을 분리하는 것을 실습하였습니다.

---

## 🛠️ 기술 스택

* **언어 (Languages)**: Kotlin, XML
* **개발 환경**: Android Studio
* **아키텍처 (Architecture)**: MVVM (Model-View-ViewModel)
* **데이터베이스 (Database)**: Room
* **비동기 처리 (Async)**: Kotlin Coroutines, LiveData

---

## 🔄 앱 구조 및 흐름

앱은 **Room DB → Repository → ViewModel → View** 흐름을 기반으로 동작합니다.

---

### 1. Room DB (Model 계층)

DB 접근을 위한 `Dao` 인터페이스를 정의합니다.
LiveData를 반환하여 데이터 변경 시 자동으로 UI에 반영되도록 합니다.

```kotlin
@Dao
interface TodoDao {
    @Query("SELECT * FROM TodoEntity")
    fun getAllData(): LiveData<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(todo: TodoEntity)

    @Query("DELETE FROM TodoEntity")
    suspend fun deleteAll()

    @Query("DELETE FROM TodoEntity WHERE id = :id")
    suspend fun deleteById(id: Int)
}
```

---

### 2. Repository

Repository는 DB 접근 로직을 캡슐화하여 ViewModel이 데이터 소스를 직접 알지 않아도 되도록 합니다.
비동기 처리를 위해 **코루틴**을 사용합니다.

```kotlin
class TodoRepository(private val dao: TodoDao) {
    val allData: LiveData<List<TodoEntity>> = dao.getAllData()

    suspend fun insert(todo: TodoEntity) {
        dao.insertData(todo)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }
}
```

---

### 3. ViewModel

ViewModel은 Repository를 통해 데이터를 가져오고, `LiveData`로 관리하여 View에 전달합니다.
UI 관련 로직과 데이터 보존 역할을 담당합니다.

```kotlin
class TodoViewModel(application: Application): AndroidViewModel(application) {
    private val repository: TodoRepository
    val allTodos: LiveData<List<TodoEntity>>

    init {
        val dao = TodoDatabase.getDatabase(application).todoDao()
        repository = TodoRepository(dao)
        allTodos = repository.allData
    }

    fun insert(todo: TodoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(todo)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun deleteById(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteById(id)
    }
}
```

💡 **AndroidViewModel을 사용한 이유**

* Room 데이터베이스 초기화 시 Application Context가 필요하기 때문입니다.
* 일반 ViewModel은 Context에 접근할 수 없으나, AndroidViewModel은 생성자를 통해 안전하게 Context를 전달받을 수 있습니다.

---

### 4. View (Activity & Adapter)

#### ViewModel 초기화 (MainActivity)

```kotlin
viewModel = ViewModelProvider(
    this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
).get(TodoViewModel::class.java)
```

#### LiveData 관찰 (자동 업데이트)

```kotlin
viewModel.allTodos.observe(this) { todos ->
    adapter.submitList(todos)
}
```

➡️ LiveData 값이 변경될 때마다 RecyclerView UI가 자동 갱신됩니다.

#### 사용자 입력 처리

```kotlin
binding.btnAdd.setOnClickListener {
    val todoText = binding.etTodo.text.toString().trim()
    if (todoText.isEmpty()) {
        Toast.makeText(this, "할 일을 입력해주세요.", Toast.LENGTH_SHORT).show()
    } else {
        val todo = TodoEntity(todo = todoText)
        viewModel.insert(todo)
    }
}
```

#### 삭제 콜백 인터페이스

```kotlin
interface OnDeleteClickListener {
    fun onDeleteClick(id: Int)
}
```

```kotlin
override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
    holder.binding.tvTodo.text = data[position].todo
    holder.binding.btnDelete.setOnClickListener {
        listener.onDeleteClick(data[position].id)
    }
}
```

➡️ 삭제 버튼 클릭 → Adapter 콜백 실행 → ViewModel의 `deleteById()` 호출 → Repository → Room DB 삭제 → LiveData 변경 → UI 자동 반영

---

## 📌 전체 데이터 흐름 요약

1. **사용자 입력 (추가/삭제)** → `MainActivity` → `ViewModel` 호출
2. **ViewModel** → `Repository` 통해 DB 요청 위임
3. **Repository** → `Room DB` 접근 (비동기 처리)
4. **DB 변경** → `LiveData` 업데이트 → `ViewModel` → `View` 자동 반영

---

## 📱 주요 기능

* Todo 추가 / 삭제
* Room DB를 통한 데이터 영구 저장
* LiveData & 코루틴을 통한 **실시간 UI 업데이트**

---

## 📊 구조 다이어그램

```text
사용자 입력
    ↓
   View (Activity / Adapter)
    ↓
 ViewModel (TodoViewModel)
    ↓
 Repository (TodoRepository)
    ↓
 Room DB (TodoDao, TodoEntity)
    ↓
 LiveData 업데이트
    ↓
 View 자동 반영 (Observer)
```

---

## 실행 화면 (Screenshots & GIFs)

<img src="screenshot/recording.gif" width="250"/>




