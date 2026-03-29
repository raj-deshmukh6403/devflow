const BASE_URL = '/api';

export async function fetchSummary() {
  const res = await fetch(`${BASE_URL}/metrics/summary`);
  return res.json();
}

export async function fetchLeaderboard() {
  const res = await fetch(`${BASE_URL}/developers/leaderboard`);
  return res.json();
}

export async function fetchPRs(from: string, to: string) {
  const res = await fetch(`${BASE_URL}/metrics/prs?from=${from}&to=${to}`);
  return res.json();
}

export async function fetchBuilds(from: string, to: string) {
  const res = await fetch(`${BASE_URL}/metrics/builds?from=${from}&to=${to}`);
  return res.json();
}