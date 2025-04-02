import React, { useState, useEffect } from 'react';

interface Task {
  id: number;
  text: string;
  completed: boolean;
}

const TodoList: React.FC = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [inputValue, setInputValue] = useState('');
  const [filter, setFilter] = useState<'all' | 'active' | 'completed'>('all');

  // Load tasks from localStorage on component mount
  useEffect(() => {
    const savedTasks = localStorage.getItem('tasks');
    if (savedTasks) {
      setTasks(JSON.parse(savedTasks));
    }
  }, []);

  // Save tasks to localStorage whenever they change
  useEffect(() => {
    localStorage.setItem('tasks', JSON.stringify(tasks));
  }, [tasks]);

  const addTask = () => {
    if (inputValue.trim()) {
      const newTask: Task = {
        id: Date.now(),
        text: inputValue,
        completed: false,
      };
      setTasks([...tasks, newTask]);
      setInputValue('');
    }
  };

  const toggleComplete = (id: number) => {
    setTasks(
      tasks.map(task =>
        task.id === id ? { ...task, completed: !task.completed } : task
      )
    );
  };

  const deleteTask = (id: number) => {
    setTasks(tasks.filter(task => task.id !== id));
  };

  const deleteSelected = () => {
    setTasks(tasks.filter(task => !task.completed));
  };

  const deleteAll = () => {
    setTasks([]);
  };

  const filteredTasks = tasks.filter(task => {
    if (filter === 'active') return !task.completed;
    if (filter === 'completed') return task.completed;
    return true;
  });

  const completedCount = tasks.filter(task => task.completed).length;

  return (
    <div className='min-h-screen flex justify-center items-center bg-gradient-to-r from-purple-100 via-pink-50 to-indigo-100'>
      <div className="max-w-md mx-auto my-8 bg-white p-8 rounded-xl shadow-md">
        <h1 className="text-2xl font-bold text-center text-purple-600 mb-6">Todo List</h1>

        {/* Input area */}
        <div className="flex mb-6">
          <input
            type="text"
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && addTask()}
            placeholder="Add your todo here..."
            className="flex-grow px-4 py-2 border-2 border-gray-200 rounded-l-lg focus:outline-none focus:border-purple-500"
          />
          <button
            onClick={addTask}
            className="bg-purple-600 hover:bg-purple-700 text-white px-4 rounded-r-lg transition-colors duration-200"
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
          </button>
        </div>

        {/* Delete buttons */}
        <div className="flex justify-between mb-6">
          <button
            onClick={deleteSelected}
            className="text-sm text-gray-600 hover:text-purple-600 transition-colors duration-200"
          >
            Delete Selected
          </button>
          <button
            onClick={deleteAll}
            className="text-sm text-gray-600 hover:text-purple-600 transition-colors duration-200"
          >
            Delete All
          </button>
        </div>

        {/* Tasks list */}
        <div className="space-y-3 mb-6">
          {filteredTasks.length === 0 ? (
            <div className="text-center py-6 text-gray-400">No tasks found</div>
          ) : (
            filteredTasks.map((task) => (
              <div
                key={task.id}
                className="flex items-center justify-between p-3 border-b border-gray-100"
              >
                <div className="flex items-center">
                  <button
                    onClick={() => toggleComplete(task.id)}
                    className={`flex-shrink-0 w-5 h-5 mr-3 rounded-full border-2 ${task.completed
                        ? 'bg-purple-600 border-purple-600'
                        : 'border-gray-300'
                      }`}
                  >
                    {task.completed && (
                      <svg className="w-full h-full text-white" viewBox="0 0 20 20" fill="currentColor">
                        <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                      </svg>
                    )}
                  </button>
                  <span className={`${task.completed ? 'line-through text-gray-400' : 'text-gray-700'}`}>
                    {task.text}
                  </span>
                </div>
                <button
                  onClick={() => deleteTask(task.id)}
                  className="text-gray-400 hover:text-red-500 transition-colors duration-200"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                </button>
              </div>
            ))
          )}
        </div>

        {/* Footer */}
        <div className="flex justify-between items-center">
          <button
            onClick={() => setFilter(filter === 'all' ? 'active' : filter === 'active' ? 'completed' : 'all')}
            className="bg-purple-600 hover:bg-purple-700 text-white py-2 px-4 rounded-lg transition-colors duration-200"
          >
            {filter === 'all' ? 'All' : filter === 'active' ? 'Active' : 'Completed'}
          </button>
          <div className="text-sm text-gray-500">
            <span>{completedCount}/{tasks.length} completed</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TodoList;
