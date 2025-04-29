import './App.css'
import {ScheduleXCalendar, useCalendarApp} from "@schedule-x/react";
import { createViewWeek, createViewMonthGrid } from '@schedule-x/calendar'
import '@schedule-x/theme-default/dist/calendar.css'
function App() {
  const calendar : CalendarApp = useCalendarApp({
    views: [
      createViewWeek(),
      createViewMonthGrid()
    ],
    events: [
      {
        id: 1,
        title: 'My new event',
        start: '2025-01-01 00:00',
        end: '2025-01-01 00:00'
      }
    ],
    selectedDate: '2025-01-01'
  })

  return (
    <>
      <div>
        <ScheduleXCalendar calendarApp={calendar}/>
      </div>
    </>
  )
}

export default App
