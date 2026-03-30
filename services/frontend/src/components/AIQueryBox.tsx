import { useState } from 'react'

export default function AIQueryBox() {
  const [question, setQuestion] = useState('')
  const [answer, setAnswer] = useState('')
  const [loading, setLoading] = useState(false)

  const BASE_URL = import.meta.env.VITE_API_URL
    ? `${import.meta.env.VITE_API_URL}/api`
    : '/api'

  const askQuestion = async () => {
    if (!question.trim()) return
    setLoading(true)
    setAnswer('')
    try {
      const res = await fetch(`${BASE_URL}/ai/query`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ question })
      })
      const data = await res.json()
      setAnswer(data.answer)
    } catch (e) {
        console.log(e);        
      setAnswer('Sorry, could not get an answer right now.')
    }
    setLoading(false)
  }

  const suggestions = [
    'Who is the most active developer?',
    'Who are the top 3 developers?',
    'How many total pull requests are there?',
  ]

  return (
    <div className="space-y-4">
      <div className="bg-gray-900 rounded-xl border border-gray-800 p-6">
        <div className="flex gap-3">
          <input
            type="text"
            value={question}
            onChange={(e) => setQuestion(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && askQuestion()}
            placeholder="Ask anything about your team..."
            className="flex-1 bg-gray-800 border border-gray-700 rounded-lg px-4 py-3 text-white placeholder-gray-500 focus:outline-none focus:border-blue-500"
          />
          <button
            onClick={askQuestion}
            disabled={loading}
            className="bg-blue-600 hover:bg-blue-700 disabled:bg-blue-800 text-white px-6 py-3 rounded-lg font-medium transition"
          >
            {loading ? '...' : 'Ask'}
          </button>
        </div>

        <div className="flex gap-2 mt-3 flex-wrap">
          {suggestions.map((s) => (
            <button
              key={s}
              onClick={() => setQuestion(s)}
              className="text-xs text-gray-400 bg-gray-800 hover:bg-gray-700 px-3 py-1 rounded-full transition"
            >
              {s}
            </button>
          ))}
        </div>
      </div>

      {answer && (
        <div className="bg-gray-900 rounded-xl border border-gray-800 p-6">
          <p className="text-gray-400 text-xs mb-2">AI Answer</p>
          <p className="text-white leading-relaxed">{answer}</p>
        </div>
      )}
    </div>
  )
}