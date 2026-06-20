
const SRMSStorage = {
  KEY: 'srms_students',
 
  getStudents() {
    try {
      return JSON.parse(localStorage.getItem(this.KEY)) || [];
    } catch {
      return [];
    }
  },
 
  saveStudent(student) {
    const students = this.getStudents();
    students.push(student);
    localStorage.setItem(this.KEY, JSON.stringify(students));
  },
 
  updateStudent(updated) {
    const students = this.getStudents().map(s =>
      s.id === updated.id ? updated : s
    );
    localStorage.setItem(this.KEY, JSON.stringify(students));
  },
 
  deleteStudent(id) {
    const students = this.getStudents().filter(s => s.id !== id);
    localStorage.setItem(this.KEY, JSON.stringify(students));
  }
};
 
// ─────────────────────────────────────────
// Grade Calculation
// ─────────────────────────────────────────
 
/**
 * @param {Array} subjects - array of { marksObtained, maxMarks }
 * @returns {{ totalObtained, totalMax, percentage, grade }}
 */
function calculateResult(subjects) {
  if (!subjects || subjects.length === 0) {
    return { totalObtained: 0, totalMax: 0, percentage: '0.00', grade: 'Fail' };
  }
 
  const totalObtained = subjects.reduce((sum, s) => sum + Number(s.marksObtained), 0);
  const totalMax      = subjects.reduce((sum, s) => sum + Number(s.maxMarks), 0);
  const percentage    = totalMax > 0
    ? ((totalObtained / totalMax) * 100).toFixed(2)
    : '0.00';
 
  const pct = parseFloat(percentage);
  let grade;
  if      (pct >= 90) grade = 'A+';
  else if (pct >= 75) grade = 'A';
  else if (pct >= 60) grade = 'B';
  else if (pct >= 40) grade = 'C';
  else                grade = 'Fail';
 
  return { totalObtained, totalMax, percentage, grade };
}
