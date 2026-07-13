// ─────────────────────────────────────────
// SRMS API client
// Talks to the Spring Boot REST backend (JDBC + MySQL) instead of localStorage.
// ─────────────────────────────────────────

const SRMSApi = {
  BASE: '/api/students',

  async _handle(response) {
    if (response.status === 204) return null;
    const data = await response.json().catch(() => null);
    if (!response.ok) {
      const message = (data && data.message) ? data.message : `Request failed (${response.status})`;
      throw new Error(message);
    }
    return data;
  },

  async getStudents() {
    const res = await fetch(this.BASE);
    return this._handle(res);
  },

  async getStudent(id) {
    const res = await fetch(`${this.BASE}/${id}`);
    return this._handle(res);
  },

  async createStudent(name, rollNumber) {
    const res = await fetch(this.BASE, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, rollNumber })
    });
    return this._handle(res);
  },

  async deleteStudent(id) {
    const res = await fetch(`${this.BASE}/${id}`, { method: 'DELETE' });
    return this._handle(res);
  },

  async addSubject(studentId, subjectName, marksObtained, maxMarks) {
    const res = await fetch(`${this.BASE}/${studentId}/subjects`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ subjectName, marksObtained, maxMarks })
    });
    return this._handle(res);
  },

  async removeSubject(studentId, subjectId) {
    const res = await fetch(`${this.BASE}/${studentId}/subjects/${subjectId}`, { method: 'DELETE' });
    return this._handle(res);
  }
};
