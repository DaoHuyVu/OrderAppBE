// Sidebar toggle

// theme toggle
const sidebarToggle = document.querySelector("#sidebar-toggle");
sidebarToggle.addEventListener("click" , function(){
  document.querySelector("#sidebar").classList.toggle("collapsed");
});

document.querySelector(".theme-toggle").addEventListener("click", () => {
  toggleLocalStorage();
  toggleRootClass();
});

function toggleRootClass() {
  const current = document.documentElement.getAttribute("data-bs-theme");
  const inverted = current == "dark" ? "light" : "dark";
  document.documentElement.setAttribute("data-bs-theme", inverted);
}

function toggleLocalStorage() {
  if (isLight()) {
    localStorage.removeItem("light");
  } else {
    localStorage.setItem("light", "set");
  }
}

function isLight() {
  return localStorage.getItem("light");
}

if (isLight()) {
  toggleRootClass();
}

document.getElementById('food').addEventListener('click', function() {
  window.location.href = 'food.html';
});

document.getElementById('branch').addEventListener('click', function() {
  window.location.href = 'branch.html';
});

document.getElementById('master').addEventListener('click', function() {
  window.location.href = 'MasterDashboard.html';
});

document.getElementById('food1').addEventListener('click', function() {
  window.location.href = 'food.html';
});

document.getElementById('branch1').addEventListener('click', function() {
  window.location.href = 'branch.html';
});