import { useState, useEffect, useRef } from 'react';

const Timer = () => {
  const [time, setTime] = useState(900); // 15 minutes in seconds
  const [isRunning, setIsRunning] = useState(false);
  const curTime = useRef(900);  // 用 useRef 存储当前时间

  useEffect(() => {
    let intervalId: number | null = null;

    if (isRunning && time > 0) {
      intervalId = setInterval(() => {
        setTime((prevTime) => prevTime - 1);
      }, 1000);
    }

    return () => {
      if (intervalId) {
        clearInterval(intervalId);
      }
    };
  }, [isRunning, time]);

  const formatTime = (timeInSeconds: number): string => {
    const minutes = Math.floor(timeInSeconds / 60);
    const seconds = timeInSeconds % 60;
    return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
  };

  const handleStart = () => {
    setIsRunning(true);
  };

  const handlePause = () => {
    setIsRunning(false);
  };

  const changeTime = (newTime: number) => {
    setTime(newTime);
    curTime.current = newTime;  // 更新 useRef 的值
    handleReset();
  };

  const handleReset = () => {
    setIsRunning(false);
    setTime(curTime.current); // 用 useRef 存储的值重置时间
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 text-white min-h-screen bg-gradient-to-r from-purple-100 via-pink-50 to-indigo-100">
      <div className="bg-white text-purple-900 p-16 rounded-2xl shadow-2xl w-[41rem] text-center space-y-8">
        <div className="flex justify-between mb-8 gap-8">
        <button onClick={() => changeTime(25 * 60)} className="px-8 py-3 bg-purple-500 text-white rounded-lg hover:bg-purple-700 transition-all">Pomodoro</button>
          <button onClick={() => changeTime(5 * 60)} className="px-8 py-3 bg-purple-500 text-white rounded-lg hover:bg-purple-700 transition-all">Short Break</button>
          <button onClick={() => changeTime(15 * 60)} className="px-8 py-3 bg-purple-500 text-white rounded-lg hover:bg-purple-700 transition-all">Long Break</button>
        </div>

        <h1 className="text-4xl font-bold">Timer</h1>

        <div className="text-8xl font-extrabold">{formatTime(time)}</div>

        <div className="flex justify-between gap-8">
          <button onClick={handleStart} className="px-8 py-3 bg-green-500 text-white rounded-lg hover:bg-green-700 transition-all disabled:opacity-50" disabled={isRunning}>Start</button>
          <button onClick={handlePause} className="px-8 py-3 bg-yellow-500 text-white rounded-lg hover:bg-yellow-700 transition-all disabled:opacity-50" disabled={!isRunning}>Pause</button>
          <button onClick={handleReset} className="px-8 py-3 bg-red-500 text-white rounded-lg hover:bg-red-700 transition-all">Reset</button>
        </div>
      </div>
    </div>
  );
};

export default Timer;
