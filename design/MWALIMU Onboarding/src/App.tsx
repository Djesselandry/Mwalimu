import { useRef, useState } from "react";

type Screen =
  | "welcome"
  | "profile"
  | "level"
  | "subjects"
  | "preferences"
  | "complete"
  | "teacher"
  | "chat"
  | "programme"
  | "evaluation"
  | "quiz-question"
  | "quiz-feedback"
  | "quiz-complete";

type IconProps = {
  children: string;
  className?: string;
};

type ChatMessage = {
  id: number;
  role: "student" | "teacher";
  text: string;
};

type CourseStatus = "completed" | "progress" | "not-started" | "locked";

type Course = {
  id: number;
  subject: string;
  title: string;
  chapter: string;
  duration: string;
  progress: number;
  status: CourseStatus;
  icon: string;
};

type QuizStatus = "not-started" | "completed" | "progress";

type Quiz = {
  id: number;
  subject: string;
  title: string;
  questions: number;
  duration: string;
  difficulty: "Facile" | "Intermédiaire" | "Avancé";
  status: QuizStatus;
  score?: number;
  icon: string;
};

type QuizQuestion = {
  number: number;
  type: "choice" | "boolean" | "written";
  label: string;
  prompt: string;
  options?: string[];
  answer: string;
  explanation: string;
};

function Icon({ children, className = "" }: IconProps) {
  return <span className={`material-symbols-outlined ${className}`}>{children}</span>;
}

const welcomeSlides = [
  {
    eyebrow: "APPRENEZ PARTOUT",
    title: "Bienvenue sur MWALIMU",
    copy: "Votre enseignant intelligent qui vous accompagne partout, même sans Internet.",
    visual: "teacher",
  },
  {
    eyebrow: "UNE QUESTION ?",
    title: "Posez toutes vos questions",
    copy: "Écrivez ou parlez naturellement. MWALIMU vous aide à comprendre chaque leçon.",
    visual: "questions",
  },
  {
    eyebrow: "AVANCEZ À VOTRE RYTHME",
    title: "Progressez chaque jour",
    copy: "Suivez vos cours, passez des évaluations et développez vos connaissances à votre rythme.",
    visual: "progress",
  },
];

const levels = [
  { title: "Middle school", caption: "Grades 6–8", icon: "menu_book" },
  { title: "High school", caption: "Grades 9–12", icon: "school" },
  { title: "University", caption: "Undergraduate & beyond", icon: "account_balance" },
  { title: "Independent learner", caption: "Learning at my own pace", icon: "explore" },
];

const dailyGoals = [
  { minutes: 10, icon: "local_cafe", text: "Un petit pas chaque jour" },
  { minutes: 20, icon: "menu_book", text: "Un rythme tout en douceur" },
  { minutes: 30, icon: "bolt", text: "Idéal pour bien progresser" },
  { minutes: 45, icon: "psychology", text: "Pour aller plus loin" },
  { minutes: 60, icon: "rocket_launch", text: "Un vrai défi quotidien" },
];

const programmeFilters = [
  "Tous",
  "En cours",
  "Terminés",
  "Non commencés",
  "Mathématiques",
  "Français",
  "Sciences",
] as const;

const courses: Course[] = [
  {
    id: 1,
    subject: "Mathématiques",
    title: "Mathématiques",
    chapter: "Chapitre 5 · Théorème de Pythagore",
    duration: "24 min",
    progress: 68,
    status: "progress",
    icon: "calculate",
  },
  {
    id: 2,
    subject: "Français",
    title: "Français",
    chapter: "Chapitre 3 · Figures de style",
    duration: "18 min",
    progress: 100,
    status: "completed",
    icon: "edit_note",
  },
  {
    id: 3,
    subject: "Sciences",
    title: "Sciences physiques",
    chapter: "Chapitre 2 · Forces et mouvement",
    duration: "32 min",
    progress: 35,
    status: "progress",
    icon: "science",
  },
  {
    id: 4,
    subject: "Mathématiques",
    title: "Algèbre",
    chapter: "Chapitre 6 · Équations du second degré",
    duration: "28 min",
    progress: 0,
    status: "not-started",
    icon: "functions",
  },
  {
    id: 5,
    subject: "Sciences",
    title: "Biologie",
    chapter: "Chapitre 4 · Génétique",
    duration: "26 min",
    progress: 0,
    status: "locked",
    icon: "genetics",
  },
];

const quizFilters = ["Tous", "Non commencés", "Terminés", "Favoris"] as const;

const quizzes: Quiz[] = [
  {
    id: 1,
    subject: "Mathématiques",
    title: "Le théorème de Pythagore",
    questions: 10,
    duration: "12 min",
    difficulty: "Intermédiaire",
    status: "progress",
    score: 70,
    icon: "calculate",
  },
  {
    id: 2,
    subject: "Français",
    title: "Les figures de style",
    questions: 15,
    duration: "18 min",
    difficulty: "Facile",
    status: "completed",
    score: 88,
    icon: "edit_note",
  },
  {
    id: 3,
    subject: "Sciences",
    title: "Forces et mouvement",
    questions: 12,
    duration: "15 min",
    difficulty: "Intermédiaire",
    status: "not-started",
    icon: "science",
  },
  {
    id: 4,
    subject: "Biologie",
    title: "La cellule vivante",
    questions: 20,
    duration: "22 min",
    difficulty: "Avancé",
    status: "completed",
    score: 76,
    icon: "genetics",
  },
];

const quizQuestions: QuizQuestion[] = [
  {
    number: 3,
    type: "choice",
    label: "Choix multiple",
    prompt: "Dans un triangle rectangle, que représente l’hypoténuse ?",
    options: [
      "Le côté opposé à l’angle droit",
      "Le côté le plus court",
      "La somme des deux autres côtés",
      "Un angle de 45 degrés",
    ],
    answer: "Le côté opposé à l’angle droit",
    explanation:
      "L’hypoténuse est toujours le côté opposé à l’angle droit. C’est aussi le côté le plus long du triangle rectangle.",
  },
  {
    number: 4,
    type: "boolean",
    label: "Vrai ou faux",
    prompt: "La formule du théorème de Pythagore est a² + b² = c².",
    options: ["Vrai", "Faux"],
    answer: "Vrai",
    explanation:
      "Exact. Dans un triangle rectangle, c représente l’hypoténuse, tandis que a et b sont les deux autres côtés.",
  },
  {
    number: 5,
    type: "written",
    label: "Réponse écrite",
    prompt: "Si a = 3 et b = 4, quelle est la longueur de l’hypoténuse c ?",
    answer: "5",
    explanation: "3² + 4² = 9 + 16 = 25. La racine carrée de 25 est 5, donc l’hypoténuse mesure 5.",
  },
  {
    number: 6,
    type: "choice",
    label: "Choix multiple",
    prompt: "Quelle égalité permet de trouver le côté a ?",
    options: ["a² = c² − b²", "a² = c² + b²", "a = b + c", "a² = b² − c²"],
    answer: "a² = c² − b²",
    explanation: "On isole a² dans la formule a² + b² = c² en soustrayant b² des deux côtés.",
  },
  {
    number: 7,
    type: "boolean",
    label: "Vrai ou faux",
    prompt: "Le théorème de Pythagore s’applique à tous les triangles.",
    options: ["Vrai", "Faux"],
    answer: "Faux",
    explanation: "Il s’applique uniquement aux triangles rectangles, qui possèdent un angle droit.",
  },
  {
    number: 8,
    type: "written",
    label: "Réponse écrite",
    prompt: "Combien vaut 6² + 8² ?",
    answer: "100",
    explanation: "6² vaut 36 et 8² vaut 64. Leur somme est donc égale à 100.",
  },
  {
    number: 9,
    type: "choice",
    label: "Choix multiple",
    prompt: "Quel triplet peut former un triangle rectangle ?",
    options: ["3, 4, 5", "2, 3, 4", "4, 5, 6", "1, 2, 3"],
    answer: "3, 4, 5",
    explanation: "3² + 4² = 9 + 16 = 25, et 5² = 25. Le triplet vérifie donc le théorème.",
  },
  {
    number: 10,
    type: "boolean",
    label: "Vrai ou faux",
    prompt: "Dans un triangle rectangle, l’hypoténuse est toujours le côté le plus long.",
    options: ["Vrai", "Faux"],
    answer: "Vrai",
    explanation: "Oui. L’hypoténuse fait face au plus grand angle du triangle : l’angle droit.",
  },
];

function WelcomeArt({ variant }: { variant: string }) {
  if (variant === "questions") {
    return (
      <div className="welcome-art questions-art" aria-hidden="true">
        <div className="scene-blob question-blob" />
        <div className="chat-bubble student-chat">
          <span>Comment ça marche ?</span>
          <Icon>mic</Icon>
        </div>
        <div className="chat-bubble teacher-chat">
          <span>Je vais t’expliquer !</span>
          <Icon>auto_awesome</Icon>
        </div>
        <div className="character student question-student">
          <div className="hair student-hair" />
          <div className="head student-head">
            <span className="eye left" />
            <span className="eye right" />
            <span className="smile" />
          </div>
          <div className="body student-body">
            <Icon>mic</Icon>
          </div>
        </div>
        <div className="ai-teacher question-teacher">
          <div className="ai-antenna" />
          <div className="ai-head">
            <span className="ai-eye left" />
            <span className="ai-eye right" />
            <span className="ai-smile" />
          </div>
          <div className="ai-body">
            <Icon>school</Icon>
          </div>
        </div>
      </div>
    );
  }

  if (variant === "progress") {
    return (
      <div className="welcome-art progress-art" aria-hidden="true">
        <div className="scene-blob progress-blob" />
        <div className="progress-board">
          <div className="board-top">
            <span>Progression</span>
            <b>84%</b>
          </div>
          <div className="mini-bars">
            <i />
            <i />
            <i />
            <i />
            <i />
          </div>
          <div className="quiz-row">
            <Icon>check_circle</Icon>
            <span>Quiz terminé</span>
          </div>
        </div>
        <div className="character student progress-student">
          <div className="hair student-hair" />
          <div className="head student-head">
            <span className="eye left" />
            <span className="eye right" />
            <span className="smile" />
          </div>
          <div className="body student-body">
            <Icon>menu_book</Icon>
          </div>
        </div>
        <div className="achievement-medal">
          <Icon>workspace_premium</Icon>
        </div>
        <div className="streak-badge">
          <Icon>local_fire_department</Icon>
          <span>7 jours</span>
        </div>
      </div>
    );
  }

  return (
    <div className="welcome-art welcome-classroom" aria-hidden="true">
      <div className="scene-blob classroom-blob" />
      <div className="classroom-board">
        <Icon>calculate</Icon>
        <span>A + B = C</span>
        <Icon>science</Icon>
      </div>
      <div className="classroom-books">
        <i />
        <i />
        <i />
      </div>
      <div className="character student welcome-student">
        <div className="hair student-hair" />
        <div className="head student-head">
          <span className="eye left" />
          <span className="eye right" />
          <span className="smile" />
        </div>
        <div className="body student-body">
          <Icon>menu_book</Icon>
        </div>
      </div>
      <div className="ai-teacher welcome-teacher">
        <div className="ai-hand wave-hand">
          <Icon>waving_hand</Icon>
        </div>
        <div className="ai-antenna" />
        <div className="ai-head">
          <span className="ai-eye left" />
          <span className="ai-eye right" />
          <span className="ai-smile" />
        </div>
        <div className="ai-body">
          <Icon>school</Icon>
        </div>
      </div>
      <div className="classroom-icon bulb-icon">
        <Icon>lightbulb</Icon>
      </div>
      <div className="classroom-icon alphabet-icon">
        <span>Aa</span>
      </div>
    </div>
  );
}

function TopBar({ step, onBack }: { step: number; onBack: () => void }) {
  return (
    <header className="top-bar">
      <button className="icon-button" onClick={onBack} aria-label="Retour">
        <Icon>arrow_back</Icon>
      </button>
      <div className="progress-wrap">
        <span>Étape {step} sur 4</span>
        <div className="progress-track" aria-label={`Étape ${step} sur 4`}>
          <div className="progress-fill" style={{ width: `${step * 25}%` }} />
        </div>
      </div>
      <div className="top-spacer" />
    </header>
  );
}

function BottomAction({
  label = "Continue",
  onClick,
  disabled,
}: {
  label?: string;
  onClick: () => void;
  disabled?: boolean;
}) {
  return (
    <div className="bottom-action">
      <button className="primary-button" onClick={onClick} disabled={disabled}>
        <span>{label}</span>
        <Icon>{label === "Finish setup" ? "check" : "arrow_forward"}</Icon>
      </button>
      <div className="gesture-bar" />
    </div>
  );
}

function BottomNavigation({
  active,
  onNavigate,
}: {
  active: "teacher" | "chat" | "programme" | "evaluation";
  onNavigate: (screen: Screen) => void;
}) {
  return (
    <nav className="teacher-bottom-nav" aria-label="Navigation principale">
      <button
        className={active === "teacher" ? "selected" : ""}
        aria-current={active === "teacher" ? "page" : undefined}
        onClick={() => onNavigate("teacher")}
      >
        <span>
          <Icon>school</Icon>
        </span>
        <small>Professeur</small>
      </button>
      <button
        className={active === "chat" ? "selected" : ""}
        aria-current={active === "chat" ? "page" : undefined}
        onClick={() => onNavigate("chat")}
      >
        <span>
          <Icon>chat_bubble</Icon>
        </span>
        <small>Chat</small>
      </button>
      <button
        className={active === "programme" ? "selected" : ""}
        aria-current={active === "programme" ? "page" : undefined}
        onClick={() => onNavigate("programme")}
      >
        <span>
          <Icon>calendar_month</Icon>
        </span>
        <small>Programme</small>
      </button>
      <button
        className={active === "evaluation" ? "selected" : ""}
        aria-current={active === "evaluation" ? "page" : undefined}
        onClick={() => onNavigate("evaluation")}
      >
        <span>
          <Icon>quiz</Icon>
        </span>
        <small>Évaluation</small>
      </button>
    </nav>
  );
}

export default function App() {
  const [screen, setScreen] = useState<Screen>("welcome");
  const [slide, setSlide] = useState(0);
  const swipeStart = useRef<number | null>(null);
  const [name, setName] = useState("");
  const [level, setLevel] = useState("");
  const [teacher, setTeacher] = useState("Professeure Amina");
  const [voice, setVoice] = useState("Voix 2");
  const [playingVoice, setPlayingVoice] = useState("");
  const [dailyGoal, setDailyGoal] = useState(30);
  const [lessonStarted, setLessonStarted] = useState(false);
  const [microphoneActive, setMicrophoneActive] = useState(false);
  const [textMode, setTextMode] = useState(false);
  const [chatInput, setChatInput] = useState("");
  const [messageReaction, setMessageReaction] = useState<"like" | "dislike" | "">("");
  const [programmeQuery, setProgrammeQuery] = useState("");
  const [programmeFilter, setProgrammeFilter] = useState<(typeof programmeFilters)[number]>("Tous");
  const programmeSearch = useRef<HTMLInputElement>(null);
  const [quizQuery, setQuizQuery] = useState("");
  const [quizFilter, setQuizFilter] = useState<(typeof quizFilters)[number]>("Tous");
  const [favoriteQuizzes, setFavoriteQuizzes] = useState<number[]>([1, 4]);
  const [quizQuestionIndex, setQuizQuestionIndex] = useState(0);
  const [quizAnswer, setQuizAnswer] = useState("");
  const [quizAnswerCorrect, setQuizAnswerCorrect] = useState(false);
  const quizSearch = useRef<HTMLInputElement>(null);
  const [chatMessages, setChatMessages] = useState<ChatMessage[]>([
    {
      id: 1,
      role: "student",
      text: "Pouvez-vous m’expliquer le théorème de Pythagore simplement ?",
    },
    {
      id: 2,
      role: "teacher",
      text: "Bien sûr, Landry. Dans un triangle rectangle, le carré de l’hypoténuse est égal à la somme des carrés des deux autres côtés.",
    },
  ]);

  const previewVoice = (voiceName: string) => {
    setVoice(voiceName);
    setPlayingVoice(voiceName);

    if ("speechSynthesis" in window) {
      window.speechSynthesis.cancel();
      const message = new SpeechSynthesisUtterance(
        voiceName === "Voix 1"
          ? "Bonjour, je serai votre guide."
          : voiceName === "Voix 2"
            ? "Bienvenue, apprenons ensemble."
            : "Prêt à commencer votre aventure ?",
      );
      message.lang = "fr-FR";
      message.pitch = voiceName === "Voix 1" ? 0.9 : voiceName === "Voix 2" ? 1.05 : 1.15;
      message.onend = () => setPlayingVoice("");
      message.onerror = () => setPlayingVoice("");
      window.speechSynthesis.speak(message);
    } else {
      setPlayingVoice("");
    }
  };

  const repeatTeacherMessage = () => {
    if (!("speechSynthesis" in window)) return;
    window.speechSynthesis.cancel();
    const message = new SpeechSynthesisUtterance(
      "Bonjour Landry ! Je suis heureuse de vous retrouver aujourd'hui. Nous allons commencer votre prochaine leçon.",
    );
    message.lang = "fr-FR";
    message.pitch = 1.05;
    window.speechSynthesis.speak(message);
  };

  const readChatMessage = (text: string) => {
    if (!("speechSynthesis" in window)) return;
    window.speechSynthesis.cancel();
    const message = new SpeechSynthesisUtterance(text);
    message.lang = "fr-FR";
    message.pitch = 1.05;
    window.speechSynthesis.speak(message);
  };

  const sendChatMessage = (suggestion?: string) => {
    const question = (suggestion ?? chatInput).trim();
    if (!question) return;

    setChatMessages((current) => [
      ...current,
      { id: Date.now(), role: "student", text: question },
      {
        id: Date.now() + 1,
        role: "teacher",
        text: "Très bonne question. Prenons le temps de la comprendre étape par étape, avec un exemple simple adapté à votre niveau.",
      },
    ]);
    setChatInput("");
    setMessageReaction("");
  };

  const back = (target: Screen) => setScreen(target);

  const visibleCourses = courses.filter((course) => {
    const query = programmeQuery.trim().toLocaleLowerCase("fr");
    const matchesQuery =
      !query ||
      course.title.toLocaleLowerCase("fr").includes(query) ||
      course.chapter.toLocaleLowerCase("fr").includes(query);
    const matchesFilter =
      programmeFilter === "Tous" ||
      (programmeFilter === "En cours" && course.status === "progress") ||
      (programmeFilter === "Terminés" && course.status === "completed") ||
      (programmeFilter === "Non commencés" && course.status === "not-started") ||
      course.subject === programmeFilter;

    return matchesQuery && matchesFilter;
  });

  const visibleQuizzes = quizzes.filter((quiz) => {
    const query = quizQuery.trim().toLocaleLowerCase("fr");
    const matchesQuery =
      !query ||
      quiz.title.toLocaleLowerCase("fr").includes(query) ||
      quiz.subject.toLocaleLowerCase("fr").includes(query);
    const matchesFilter =
      quizFilter === "Tous" ||
      (quizFilter === "Non commencés" && quiz.status === "not-started") ||
      (quizFilter === "Terminés" && quiz.status === "completed") ||
      (quizFilter === "Favoris" && favoriteQuizzes.includes(quiz.id));

    return matchesQuery && matchesFilter;
  });

  const activeQuizQuestion = quizQuestions[quizQuestionIndex];

  const startQuiz = () => {
    setQuizQuestionIndex(0);
    setQuizAnswer("");
    setQuizAnswerCorrect(false);
    setScreen("quiz-question");
  };

  const validateQuizAnswer = () => {
    if (!quizAnswer.trim()) return;
    setQuizAnswerCorrect(
      quizAnswer.trim().toLocaleLowerCase("fr") ===
        activeQuizQuestion.answer.trim().toLocaleLowerCase("fr"),
    );
    setScreen("quiz-feedback");
  };

  const nextQuizQuestion = () => {
    if (quizQuestionIndex === quizQuestions.length - 1) {
      setScreen("quiz-complete");
      return;
    }
    setQuizQuestionIndex((current) => current + 1);
    setQuizAnswer("");
    setScreen("quiz-question");
  };

  const finishSwipe = (clientX: number) => {
    if (swipeStart.current === null) return;
    const distance = clientX - swipeStart.current;
    if (Math.abs(distance) > 45) {
      setSlide((current) => (distance < 0 ? Math.min(current + 1, 2) : Math.max(current - 1, 0)));
    }
    swipeStart.current = null;
  };

  return (
    <main className="app-shell">
      <section className="phone-frame">
        {screen === "welcome" && (
          <div
            className="screen welcome-screen"
            onPointerDown={(event) => {
              swipeStart.current = event.clientX;
            }}
            onPointerUp={(event) => finishSwipe(event.clientX)}
            onPointerCancel={() => {
              swipeStart.current = null;
            }}
          >
            <div className="brand">
              <div className="brand-mark">
                <Icon>school</Icon>
              </div>
              <span>MWALIMU</span>
            </div>
            <div key={slide} className="slide-stage">
              <WelcomeArt variant={welcomeSlides[slide].visual} />
              <div className="welcome-content">
                <span className="eyebrow">{welcomeSlides[slide].eyebrow}</span>
                <h1>{welcomeSlides[slide].title}</h1>
                <p>{welcomeSlides[slide].copy}</p>
              </div>
            </div>
            <div className="welcome-footer">
              <div className="pagination" aria-label={`Page ${slide + 1} sur 3`}>
                {welcomeSlides.map((item, index) => (
                  <button
                    key={item.title}
                    aria-label={`Aller à la page ${index + 1}`}
                    className={slide === index ? "active" : ""}
                    onClick={() => setSlide(index)}
                  />
                ))}
              </div>
              <button
                className="primary-button"
                onClick={() => {
                  if (slide < welcomeSlides.length - 1) setSlide(slide + 1);
                  else setScreen("profile");
                }}
              >
                <span>{slide === 2 ? "Commencer" : "Continuer"}</span>
                <Icon>arrow_forward</Icon>
              </button>
              {slide < 2 && (
                <button className="text-button" onClick={() => setScreen("profile")}>
                  Passer
                </button>
              )}
            </div>
          </div>
        )}

        {screen === "profile" && (
          <div className="screen setup-screen">
            <TopBar step={2} onBack={() => back("welcome")} />
            <div className="screen-content profile-content">
              <div className="profile-heading">
                <h2>Parlez-nous de vous</h2>
                <p className="subtitle">
                  Ces informations permettront de personnaliser votre apprentissage.
                </p>
              </div>
              <div className="profile-illustration" aria-hidden="true">
                <div className="profile-illustration-blob" />
                <div className="profile-book">
                  <Icon>auto_stories</Icon>
                </div>
                <div className="profile-star">
                  <Icon>star</Icon>
                </div>
                <div className="profile-student">
                  <div className="profile-hair" />
                  <div className="profile-face">
                    <i />
                    <i />
                    <span />
                  </div>
                  <div className="profile-shirt">
                    <Icon>school</Icon>
                  </div>
                </div>
                <div className="profile-pencil">
                  <Icon>edit</Icon>
                </div>
              </div>
              <label className={`filled-field ${name ? "has-value" : ""}`}>
                <span>Prénom ou pseudonyme</span>
                <input
                  value={name}
                  onChange={(event) => setName(event.target.value)}
                  placeholder="Ex : Aline"
                  autoComplete="given-name"
                />
                {name && <Icon>check_circle</Icon>}
              </label>
              <label className={`filled-field select-field ${level ? "has-value" : ""}`}>
                <span>Niveau scolaire</span>
                <select value={level} onChange={(event) => setLevel(event.target.value)} required>
                  <option value="" disabled>
                    Sélectionnez votre niveau
                  </option>
                  <option value="7e">7e</option>
                  <option value="8e">8e</option>
                  <option value="9e">9e</option>
                  <option value="10e">10e</option>
                  <option value="1ère">1ère</option>
                  <option value="2ème">2ème</option>
                  <option value="3ème">3ème</option>
                  <option value="Université">Université</option>
                </select>
                <Icon>expand_more</Icon>
              </label>
              <p className="profile-privacy">
                <Icon>lock</Icon>
                Vos informations restent privées sur cet appareil.
              </p>
            </div>
            <BottomAction
              label="Continuer"
              onClick={() => setScreen("subjects")}
              disabled={!name.trim() || !level}
            />
          </div>
        )}

        {screen === "level" && (
          <div className="screen setup-screen">
            <TopBar step={2} onBack={() => back("profile")} />
            <div className="screen-content compact">
              <div className="step-icon green">
                <Icon>school</Icon>
              </div>
              <h2>Where are you in your learning journey?</h2>
              <p className="subtitle">We’ll match explanations and examples to your level.</p>
              <div className="option-list">
                {levels.map((item) => (
                  <button
                    key={item.title}
                    className={`option-card ${level === item.title ? "selected" : ""}`}
                    onClick={() => setLevel(item.title)}
                  >
                    <span className="option-icon">
                      <Icon>{item.icon}</Icon>
                    </span>
                    <span className="option-copy">
                      <strong>{item.title}</strong>
                      <small>{item.caption}</small>
                    </span>
                    <span className="radio">
                      <span />
                    </span>
                  </button>
                ))}
              </div>
            </div>
            <BottomAction onClick={() => setScreen("subjects")} disabled={!level} />
          </div>
        )}

        {screen === "subjects" && (
          <div className="screen setup-screen">
            <TopBar step={3} onBack={() => back("profile")} />
            <div className="screen-content teacher-step-content">
              <div className="teacher-step-heading">
                <span className="teacher-heading-icon">
                  <Icon>record_voice_over</Icon>
                </span>
                <div>
                  <h2>Choisissez votre enseignant</h2>
                  <p className="subtitle">
                    Vous pourrez modifier ce choix à tout moment dans les paramètres.
                  </p>
                </div>
              </div>
              <div className="teacher-card-grid" role="radiogroup" aria-label="Enseignant">
                <button
                  className={`teacher-choice-card ${
                    teacher === "Professeur David" ? "selected" : ""
                  }`}
                  onClick={() => setTeacher("Professeur David")}
                  role="radio"
                  aria-checked={teacher === "Professeur David"}
                >
                  <span className="teacher-choice-check">
                    <Icon>check</Icon>
                  </span>
                  <span className="teacher-portrait david">
                    <span className="portrait-hair" />
                    <span className="portrait-face">
                      <i />
                      <i />
                      <b />
                    </span>
                    <span className="portrait-body">
                      <i className="shirt-collar left" />
                      <i className="shirt-collar right" />
                      <b className="teacher-tie" />
                    </span>
                  </span>
                  <strong>Professeur David</strong>
                  <small>
                    Calme <i>•</i> Encourageant
                  </small>
                </button>
                <button
                  className={`teacher-choice-card ${
                    teacher === "Professeure Amina" ? "selected" : ""
                  }`}
                  onClick={() => setTeacher("Professeure Amina")}
                  role="radio"
                  aria-checked={teacher === "Professeure Amina"}
                >
                  <span className="teacher-choice-check">
                    <Icon>check</Icon>
                  </span>
                  <span className="teacher-portrait amina">
                    <span className="portrait-hair" />
                    <span className="portrait-face">
                      <i />
                      <i />
                      <b />
                    </span>
                    <span className="portrait-body">
                      <i className="shirt-collar left" />
                      <i className="shirt-collar right" />
                      <b className="teacher-necklace" />
                    </span>
                  </span>
                  <strong>Professeure Amina</strong>
                  <small>
                    Patiente <i>•</i> Pédagogue
                  </small>
                </button>
              </div>
              <section className="voice-section">
                <div className="voice-section-title">
                  <Icon>graphic_eq</Icon>
                  <h3>Choisissez une voix</h3>
                </div>
                <div className="voice-card-list" role="radiogroup" aria-label="Voix">
                  {["Voix 1", "Voix 2", "Voix 3"].map((voiceName, index) => (
                    <div
                      key={voiceName}
                      className={`voice-card ${voice === voiceName ? "selected" : ""}`}
                      onClick={() => setVoice(voiceName)}
                      role="radio"
                      aria-checked={voice === voiceName}
                      tabIndex={0}
                      onKeyDown={(event) => {
                        if (event.key === "Enter" || event.key === " ") {
                          event.preventDefault();
                          setVoice(voiceName);
                        }
                      }}
                    >
                      <span className="voice-speaker">
                        <Icon>volume_up</Icon>
                      </span>
                      <span className="voice-name">
                        <strong>{voiceName}</strong>
                        <small>
                          {index === 0 ? "Posée" : index === 1 ? "Chaleureuse" : "Dynamique"}
                        </small>
                      </span>
                      <button
                        type="button"
                        className={`voice-preview ${playingVoice === voiceName ? "playing" : ""}`}
                        onClick={(event) => {
                          event.stopPropagation();
                          previewVoice(voiceName);
                        }}
                        aria-label={`Écouter ${voiceName}`}
                      >
                        <Icon>{playingVoice === voiceName ? "graphic_eq" : "play_arrow"}</Icon>
                        <span>{playingVoice === voiceName ? "Lecture" : "Écouter"}</span>
                      </button>
                    </div>
                  ))}
                </div>
              </section>
            </div>
            <BottomAction
              label="Continuer"
              onClick={() => setScreen("preferences")}
              disabled={!teacher || !voice}
            />
          </div>
        )}

        {screen === "preferences" && (
          <div className="screen setup-screen">
            <TopBar step={4} onBack={() => back("subjects")} />
            <div className="screen-content final-step-content">
              <div className="final-heading">
                <span className="final-heading-icon">
                  <Icon>flag</Icon>
                </span>
                <div>
                  <h2>Quel est votre objectif quotidien ?</h2>
                  <p className="subtitle">
                    Choisissez le temps que vous souhaitez consacrer chaque jour à vos études.
                  </p>
                </div>
              </div>
              <div className="daily-goal-grid" role="radiogroup" aria-label="Objectif quotidien">
                {dailyGoals.map((item) => (
                  <button
                    key={item.minutes}
                    className={`daily-goal-card ${dailyGoal === item.minutes ? "selected" : ""}`}
                    onClick={() => setDailyGoal(item.minutes)}
                    role="radio"
                    aria-checked={dailyGoal === item.minutes}
                  >
                    <span className="daily-goal-icon">
                      <Icon>{item.icon}</Icon>
                    </span>
                    <strong>{item.minutes} minutes</strong>
                    <small>{item.text}</small>
                    <span className="daily-check">
                      <Icon>check</Icon>
                    </span>
                  </button>
                ))}
              </div>
              <section className="profile-summary">
                <div className="summary-heading">
                  <span className="summary-icon">
                    <Icon>person</Icon>
                  </span>
                  <div>
                    <span>Récapitulatif</span>
                    <h3>Profil de l’élève</h3>
                  </div>
                  <Icon className="summary-verified">verified</Icon>
                </div>
                <div className="summary-grid">
                  <div>
                    <span>Élève</span>
                    <strong>Landry</strong>
                  </div>
                  <div>
                    <span>Niveau</span>
                    <strong>Université</strong>
                  </div>
                  <div>
                    <span>Enseignante</span>
                    <strong>{teacher}</strong>
                  </div>
                  <div>
                    <span>Voix</span>
                    <strong>{voice}</strong>
                  </div>
                </div>
                <div className="summary-goal">
                  <span>
                    <Icon>schedule</Icon>
                    Objectif quotidien
                  </span>
                  <strong>{dailyGoal} min</strong>
                </div>
              </section>
            </div>
            <div className="final-bottom-action">
              <button className="primary-button" onClick={() => setScreen("complete")}>
                <span>Commencer l’aventure</span>
                <Icon>rocket_launch</Icon>
              </button>
              <p>Vous pourrez modifier ces paramètres plus tard.</p>
              <div className="gesture-bar" />
            </div>
          </div>
        )}

        {screen === "complete" && (
          <div className="screen complete-screen">
            <div className="complete-art" aria-hidden="true">
              <div className="success-ring">
                <Icon>check</Icon>
              </div>
              <span className="confetti c1" />
              <span className="confetti c2" />
              <span className="confetti c3" />
              <span className="confetti c4" />
              <span className="confetti c5" />
            </div>
            <div className="complete-copy">
              <span className="eyebrow">COMPTE CRÉÉ AVEC SUCCÈS</span>
              <h2>Bienvenue dans MWALIMU, {name || "Landry"} !</h2>
              <p>
                Votre espace d’apprentissage est prêt. Votre enseignant vous attend pour commencer
                l’aventure.
              </p>
            </div>
            <div className="offline-card">
              <span className="offline-card-icon">
                <Icon>offline_bolt</Icon>
              </span>
              <div>
                <strong>Prêt à apprendre hors ligne</strong>
                <p>Vos premières leçons sont disponibles sans Internet.</p>
              </div>
              <Icon className="verified-icon">verified</Icon>
            </div>
            <div className="complete-action">
              <button className="primary-button" onClick={() => setScreen("teacher")}>
                <span>Commencer à apprendre</span>
                <Icon>arrow_forward</Icon>
              </button>
              <p>
                <Icon>lock</Icon> Votre progression reste protégée sur cet appareil
              </p>
              <div className="gesture-bar" />
            </div>
          </div>
        )}

        {screen === "teacher" && (
          <div className="screen teacher-home-screen">
            <header className="teacher-home-appbar">
              <div>
                <h1>
                  Bonjour, Landry <span aria-hidden="true">👋</span>
                </h1>
                <p>Prêt à apprendre aujourd&apos;hui ?</p>
              </div>
              <div className="appbar-actions">
                <button aria-label="Notifications">
                  <Icon>notifications</Icon>
                  <span className="notification-dot" />
                </button>
                <button aria-label="Paramètres">
                  <Icon>settings</Icon>
                </button>
              </div>
            </header>

            <div className="teacher-home-content">
              <section className="teacher-hero" aria-label="Professeure Amina">
                <div className="teacher-hero-orbit orbit-large" />
                <div className="teacher-hero-orbit orbit-small" />
                <div className="home-teacher-portrait" aria-hidden="true">
                  <div className="home-teacher-hair" />
                  <div className="home-teacher-face">
                    <i />
                    <i />
                    <span />
                  </div>
                  <div className="home-teacher-body">
                    <span className="home-collar left" />
                    <span className="home-collar right" />
                    <Icon>school</Icon>
                  </div>
                </div>
                <span className="hero-float-icon hero-book">
                  <Icon>auto_stories</Icon>
                </span>
                <span className="hero-float-icon hero-spark">
                  <Icon>auto_awesome</Icon>
                </span>
              </section>

              <div className="teacher-identity">
                <h2>Professeure Amina</h2>
                <span className={lessonStarted ? "active" : ""}>
                  <i />
                  {lessonStarted ? "En cours" : "En attente"}
                </span>
              </div>

              <section className="subtitle-card">
                <div className="subtitle-card-heading">
                  <span>
                    <Icon>closed_caption</Icon>
                    Sous-titres
                  </span>
                  <Icon className="subtitle-equalizer">
                    {lessonStarted ? "graphic_eq" : "more_horiz"}
                  </Icon>
                </div>
                <p>
                  <strong>Bonjour Landry !</strong>
                  <span>Je suis heureuse de vous retrouver aujourd&apos;hui.</span>
                  <span>Nous allons commencer votre prochaine leçon.</span>
                </p>
                {textMode && (
                  <div className="text-mode-bar">
                    <Icon>chat</Icon>
                    <span>Mode texte activé</span>
                    <Icon>keyboard</Icon>
                  </div>
                )}
              </section>

              <section className="teacher-controls" aria-label="Commandes de la leçon">
                <button onClick={repeatTeacherMessage} aria-label="Répéter">
                  <span>
                    <Icon>replay</Icon>
                  </span>
                  <small>Répéter</small>
                </button>
                <button
                  className={`start-control ${lessonStarted ? "active" : ""}`}
                  onClick={() => setLessonStarted((current) => !current)}
                  aria-label={lessonStarted ? "Mettre en pause" : "Démarrer"}
                >
                  <span>
                    <Icon>{lessonStarted ? "pause" : "play_arrow"}</Icon>
                  </span>
                  <small>{lessonStarted ? "Pause" : "Démarrer"}</small>
                </button>
                <button
                  className={microphoneActive ? "control-selected" : ""}
                  onClick={() => setMicrophoneActive((current) => !current)}
                  aria-label="Microphone"
                >
                  <span>
                    <Icon>{microphoneActive ? "mic" : "mic_none"}</Icon>
                  </span>
                  <small>Micro</small>
                </button>
                <button
                  className={textMode ? "control-selected" : ""}
                  onClick={() => setTextMode((current) => !current)}
                  aria-label="Mode texte"
                >
                  <span>
                    <Icon>chat_bubble</Icon>
                  </span>
                  <small>Texte</small>
                </button>
              </section>

              <section className="learning-stats" aria-label="Progression">
                <article>
                  <span className="stat-icon courses">
                    <Icon>menu_book</Icon>
                  </span>
                  <div>
                    <small>Cours</small>
                    <strong>12</strong>
                  </div>
                </article>
                <article>
                  <span className="stat-icon streak">
                    <Icon>local_fire_department</Icon>
                  </span>
                  <div>
                    <small>Série</small>
                    <strong>7 jours</strong>
                  </div>
                </article>
                <article>
                  <span className="stat-icon time">
                    <Icon>schedule</Icon>
                  </span>
                  <div>
                    <small>Étude</small>
                    <strong>35 min</strong>
                  </div>
                </article>
              </section>
            </div>

            <BottomNavigation active="teacher" onNavigate={setScreen} />
          </div>
        )}

        {screen === "programme" && (
          <div className="screen programme-screen">
            <header className="programme-appbar">
              <h1>Programme</h1>
              <div>
                <button
                  className="programme-icon-button"
                  onClick={() => programmeSearch.current?.focus()}
                  aria-label="Rechercher un cours"
                >
                  <Icon>search</Icon>
                </button>
                <button className="programme-icon-button" aria-label="Paramètres">
                  <Icon>settings</Icon>
                </button>
              </div>
            </header>

            <main className="programme-content">
              <section className="programme-welcome">
                <span>
                  Bonjour, {name || "Landry"} <span aria-hidden="true">👋</span>
                </span>
                <p>Continuez votre apprentissage.</p>
              </section>

              <section className="overall-progress-card" aria-label="Progression générale : 72%">
                <div className="overall-progress-copy">
                  <span>VOTRE PROGRESSION</span>
                  <h2>Un bel élan, continuez !</h2>
                  <div className="progress-metrics">
                    <div>
                      <strong>18</strong>
                      <span>Cours terminés</span>
                    </div>
                    <i />
                    <div>
                      <strong>7</strong>
                      <span>Cours restants</span>
                    </div>
                  </div>
                </div>
                <div className="overall-progress-ring" aria-hidden="true">
                  <div>
                    <strong>72%</strong>
                    <span>Global</span>
                  </div>
                </div>
              </section>

              <section className="resume-course-card">
                <span className="resume-course-icon">
                  <Icon>calculate</Icon>
                </span>
                <div>
                  <small>REPRENDRE VOTRE DERNIER COURS</small>
                  <h2>Mathématiques</h2>
                  <p>Chapitre 5 · Théorème de Pythagore</p>
                </div>
                <button
                  onClick={() => {
                    setLessonStarted(true);
                    setScreen("teacher");
                  }}
                >
                  Continuer
                  <Icon>arrow_forward</Icon>
                </button>
              </section>

              <label className="programme-search">
                <Icon>search</Icon>
                <input
                  ref={programmeSearch}
                  value={programmeQuery}
                  onChange={(event) => setProgrammeQuery(event.target.value)}
                  placeholder="Rechercher un cours..."
                />
                {programmeQuery && (
                  <button
                    onClick={() => setProgrammeQuery("")}
                    aria-label="Effacer la recherche"
                    type="button"
                  >
                    <Icon>close</Icon>
                  </button>
                )}
              </label>

              <div className="programme-filters" aria-label="Filtrer les cours">
                {programmeFilters.map((filter) => (
                  <button
                    key={filter}
                    className={programmeFilter === filter ? "selected" : ""}
                    onClick={() => setProgrammeFilter(filter)}
                    aria-pressed={programmeFilter === filter}
                  >
                    {filter}
                  </button>
                ))}
              </div>

              <section className="course-list-section">
                <div className="programme-section-heading">
                  <div>
                    <h2>Mes cours</h2>
                    <span>{visibleCourses.length} cours disponibles</span>
                  </div>
                  <button
                    onClick={() => {
                      setProgrammeFilter("Tous");
                      setProgrammeQuery("");
                    }}
                  >
                    Tout voir
                  </button>
                </div>

                <div className="course-list">
                  {visibleCourses.map((course) => (
                    <article className={`course-card course-${course.status}`} key={course.id}>
                      <span className="course-subject-icon">
                        <Icon>{course.icon}</Icon>
                      </span>
                      <div className="course-card-content">
                        <div className="course-card-title">
                          <div>
                            <h3>{course.title}</h3>
                            <p>{course.chapter}</p>
                          </div>
                          <span className={`course-status ${course.status}`}>
                            {course.status === "completed"
                              ? "Terminé"
                              : course.status === "progress"
                                ? "En cours"
                                : course.status === "locked"
                                  ? "Verrouillé"
                                  : "À commencer"}
                          </span>
                        </div>
                        <div className="course-meta">
                          <span>
                            <Icon>schedule</Icon>
                            {course.duration}
                          </span>
                          <strong>{course.progress}%</strong>
                        </div>
                        <div className="course-progress-track">
                          <span style={{ width: `${course.progress}%` }} />
                        </div>
                        <button
                          disabled={course.status === "locked"}
                          onClick={() => {
                            if (course.status !== "locked") {
                              setLessonStarted(true);
                              setScreen("teacher");
                            }
                          }}
                        >
                          <Icon>
                            {course.status === "locked"
                              ? "lock"
                              : course.status === "completed"
                                ? "replay"
                                : course.status === "not-started"
                                  ? "play_arrow"
                                  : "arrow_forward"}
                          </Icon>
                          {course.status === "locked"
                            ? "Indisponible"
                            : course.status === "completed"
                              ? "Revoir"
                              : course.status === "not-started"
                                ? "Commencer"
                                : "Continuer"}
                        </button>
                      </div>
                    </article>
                  ))}
                  {visibleCourses.length === 0 && (
                    <div className="programme-empty">
                      <span>
                        <Icon>search_off</Icon>
                      </span>
                      <strong>Aucun cours trouvé</strong>
                      <p>Essayez un autre mot-clé ou filtre.</p>
                    </div>
                  )}
                </div>
              </section>

              <section className="daily-progress-card">
                <span className="daily-progress-icon">
                  <Icon>local_fire_department</Icon>
                </span>
                <div>
                  <span>Objectif du jour</span>
                  <strong>Encore 10 minutes, Landry !</strong>
                  <div className="daily-progress-track">
                    <span />
                  </div>
                </div>
                <strong>
                  20 <small>/ 30 min</small>
                </strong>
              </section>
            </main>

            <BottomNavigation active="programme" onNavigate={setScreen} />
          </div>
        )}

        {screen === "evaluation" && (
          <div className="screen evaluation-screen">
            <header className="evaluation-appbar">
              <div>
                <h1>Évaluation</h1>
                <p>Testez vos connaissances, sans pression.</p>
              </div>
              <button className="evaluation-icon-button" aria-label="Paramètres">
                <Icon>settings</Icon>
              </button>
            </header>

            <main className="evaluation-content">
              <section className="featured-quiz-card">
                <div className="featured-quiz-top">
                  <span className="featured-quiz-icon">
                    <Icon>bolt</Icon>
                  </span>
                  <span>QUIZ EN COURS</span>
                  <span>3 / 10</span>
                </div>
                <h2>Continuez votre dernier quiz</h2>
                <p>Mathématiques · Théorème de Pythagore</p>
                <div className="featured-quiz-progress">
                  <span />
                </div>
                <button onClick={startQuiz}>
                  Reprendre
                  <Icon>arrow_forward</Icon>
                </button>
              </section>

              <label className="quiz-search">
                <Icon>search</Icon>
                <input
                  ref={quizSearch}
                  value={quizQuery}
                  onChange={(event) => setQuizQuery(event.target.value)}
                  placeholder="Rechercher un quiz..."
                />
                {quizQuery && (
                  <button type="button" onClick={() => setQuizQuery("")} aria-label="Effacer">
                    <Icon>close</Icon>
                  </button>
                )}
              </label>

              <div className="quiz-filters" aria-label="Filtrer les quiz">
                {quizFilters.map((filter) => (
                  <button
                    key={filter}
                    className={quizFilter === filter ? "selected" : ""}
                    onClick={() => setQuizFilter(filter)}
                    aria-pressed={quizFilter === filter}
                  >
                    {filter}
                  </button>
                ))}
              </div>

              <section className="quiz-list-section">
                <div className="quiz-list-heading">
                  <div>
                    <h2>Quiz disponibles</h2>
                    <span>{visibleQuizzes.length} évaluations hors ligne</span>
                  </div>
                  <span className="offline-ready">
                    <Icon>cloud_off</Icon>
                    Hors ligne
                  </span>
                </div>

                <div className="quiz-list">
                  {visibleQuizzes.map((quiz) => (
                    <article className={`quiz-card quiz-${quiz.status}`} key={quiz.id}>
                      <span className="quiz-subject-icon">
                        <Icon>{quiz.icon}</Icon>
                      </span>
                      <div className="quiz-card-main">
                        <div className="quiz-card-heading">
                          <div>
                            <span>{quiz.subject}</span>
                            <h3>{quiz.title}</h3>
                          </div>
                          <button
                            className={favoriteQuizzes.includes(quiz.id) ? "favorite" : ""}
                            onClick={() =>
                              setFavoriteQuizzes((current) =>
                                current.includes(quiz.id)
                                  ? current.filter((id) => id !== quiz.id)
                                  : [...current, quiz.id],
                              )
                            }
                            aria-label={
                              favoriteQuizzes.includes(quiz.id)
                                ? "Retirer des favoris"
                                : "Ajouter aux favoris"
                            }
                          >
                            <Icon>favorite</Icon>
                          </button>
                        </div>
                        <div className="quiz-card-meta">
                          <span>
                            <Icon>help</Icon>
                            {quiz.questions} questions
                          </span>
                          <span>
                            <Icon>schedule</Icon>
                            {quiz.duration}
                          </span>
                        </div>
                        <div className="quiz-card-footer">
                          <span className={`difficulty ${quiz.difficulty.toLowerCase()}`}>
                            {quiz.difficulty}
                          </span>
                          {quiz.score && (
                            <span className="previous-score">
                              <Icon>verified</Icon>
                              Meilleur score : {quiz.score}%
                            </span>
                          )}
                          <button onClick={startQuiz}>
                            {quiz.status === "progress"
                              ? "Reprendre"
                              : quiz.status === "completed"
                                ? "Recommencer"
                                : "Commencer"}
                          </button>
                        </div>
                      </div>
                    </article>
                  ))}
                  {visibleQuizzes.length === 0 && (
                    <div className="quiz-empty">
                      <Icon>quiz</Icon>
                      <strong>Aucun quiz trouvé</strong>
                      <p>Modifiez votre recherche ou choisissez un autre filtre.</p>
                    </div>
                  )}
                </div>
              </section>
            </main>

            <BottomNavigation active="evaluation" onNavigate={setScreen} />
          </div>
        )}

        {screen === "quiz-question" && (
          <div className="screen quiz-question-screen">
            <header className="quiz-flow-appbar">
              <button onClick={() => setScreen("evaluation")} aria-label="Quitter le quiz">
                <Icon>close</Icon>
              </button>
              <div>
                <span>Mathématiques</span>
                <strong>Théorème de Pythagore</strong>
              </div>
              <button aria-label="Plus d’options">
                <Icon>more_vert</Icon>
              </button>
            </header>

            <main className="quiz-question-content">
              <section className="question-progress-section">
                <div>
                  <span>
                    Question <strong>{activeQuizQuestion.number}</strong> sur 10
                  </span>
                  <span>{activeQuizQuestion.number * 10}%</span>
                </div>
                <div className="question-progress-track">
                  <span style={{ width: `${activeQuizQuestion.number * 10}%` }} />
                </div>
              </section>

              <section className="question-card">
                <div className="question-card-type">
                  <span>
                    <Icon>
                      {activeQuizQuestion.type === "choice"
                        ? "checklist"
                        : activeQuizQuestion.type === "boolean"
                          ? "rule"
                          : "edit"}
                    </Icon>
                    {activeQuizQuestion.label}
                  </span>
                  <button aria-label="Écouter la question">
                    <Icon>volume_up</Icon>
                  </button>
                </div>
                <h1>{activeQuizQuestion.prompt}</h1>
                <p>
                  {activeQuizQuestion.type === "written"
                    ? "Saisissez votre réponse ci-dessous."
                    : "Sélectionnez la bonne réponse."}
                </p>
              </section>

              {activeQuizQuestion.type === "written" ? (
                <label className="written-answer-card">
                  <span>Votre réponse</span>
                  <div>
                    <input
                      value={quizAnswer}
                      onChange={(event) => setQuizAnswer(event.target.value)}
                      placeholder="Écrivez votre réponse..."
                      onKeyDown={(event) => {
                        if (event.key === "Enter") validateQuizAnswer();
                      }}
                    />
                    <Icon>edit</Icon>
                  </div>
                </label>
              ) : (
                <div
                  className={`answer-options ${activeQuizQuestion.type}`}
                  role="radiogroup"
                  aria-label="Réponses"
                >
                  {activeQuizQuestion.options?.map((option, index) => (
                    <button
                      key={option}
                      className={quizAnswer === option ? "selected" : ""}
                      onClick={() => setQuizAnswer(option)}
                      role="radio"
                      aria-checked={quizAnswer === option}
                    >
                      <span className="answer-marker">
                        {activeQuizQuestion.type === "boolean" ? (
                          <Icon>{index === 0 ? "check" : "close"}</Icon>
                        ) : (
                          String.fromCharCode(65 + index)
                        )}
                      </span>
                      <span>{option}</span>
                      <Icon className="answer-check">check_circle</Icon>
                    </button>
                  ))}
                </div>
              )}

              <section className="quiz-question-tip">
                <Icon>lightbulb</Icon>
                <p>Prenez votre temps. Se tromper fait aussi partie de l’apprentissage.</p>
              </section>
            </main>

            <div className="quiz-question-action">
              <button
                className="quiz-primary-button"
                onClick={validateQuizAnswer}
                disabled={!quizAnswer.trim()}
              >
                Valider ma réponse
                <Icon>arrow_forward</Icon>
              </button>
              <span>
                <Icon>timer</Icon>
                08:42 restantes
              </span>
            </div>
          </div>
        )}

        {screen === "quiz-feedback" && (
          <div
            className={`screen quiz-feedback-screen ${quizAnswerCorrect ? "correct" : "incorrect"}`}
          >
            <header className="feedback-appbar">
              <span>Question {activeQuizQuestion.number} sur 10</span>
              <button onClick={() => setScreen("evaluation")} aria-label="Quitter">
                <Icon>close</Icon>
              </button>
            </header>

            <main className="feedback-content">
              <section className="feedback-hero">
                <div className="feedback-illustration" aria-hidden="true">
                  <span className="feedback-ring" />
                  <span className="feedback-main-icon">
                    <Icon>{quizAnswerCorrect ? "check" : "close"}</Icon>
                  </span>
                  <Icon className="feedback-spark spark-one">auto_awesome</Icon>
                  <Icon className="feedback-spark spark-two">
                    {quizAnswerCorrect ? "star" : "favorite"}
                  </Icon>
                </div>
                <span>{quizAnswerCorrect ? "BRAVO !" : "BEL ESSAI !"}</span>
                <h1>{quizAnswerCorrect ? "Correct !" : "Pas tout à fait"}</h1>
                <p>
                  {quizAnswerCorrect
                    ? "Vous maîtrisez bien cette notion."
                    : "Chaque erreur vous rapproche de la bonne réponse."}
                </p>
              </section>

              {!quizAnswerCorrect && (
                <section className="correct-answer-card">
                  <span>
                    <Icon>check_circle</Icon>
                    Bonne réponse
                  </span>
                  <strong>{activeQuizQuestion.answer}</strong>
                  <small>Votre réponse : {quizAnswer}</small>
                </section>
              )}

              <section className="teacher-explanation-card">
                <div className="explanation-teacher">
                  <span>
                    <Icon>school</Icon>
                  </span>
                  <div>
                    <strong>Professeure Amina</strong>
                    <small>Explication</small>
                  </div>
                </div>
                <p>{activeQuizQuestion.explanation}</p>
                <div className="explanation-formula">
                  <Icon>functions</Icon>
                  <span>a² + b² = c²</span>
                </div>
              </section>

              {!quizAnswerCorrect && (
                <section className="study-tip-card">
                  <span>
                    <Icon>tips_and_updates</Icon>
                  </span>
                  <div>
                    <strong>Astuce d’apprentissage</strong>
                    <p>
                      Repérez d’abord l’angle droit : le côté juste en face est toujours
                      l’hypoténuse.
                    </p>
                  </div>
                </section>
              )}
            </main>

            <div className="feedback-actions">
              {!quizAnswerCorrect && (
                <button className="quiz-secondary-button" onClick={() => setScreen("programme")}>
                  <Icon>menu_book</Icon>
                  Revoir la leçon
                </button>
              )}
              <button className="quiz-primary-button" onClick={nextQuizQuestion}>
                {activeQuizQuestion.number === 10 ? "Voir mes résultats" : "Question suivante"}
                <Icon>arrow_forward</Icon>
              </button>
            </div>
          </div>
        )}

        {screen === "quiz-complete" && (
          <div className="screen quiz-results-screen">
            <header className="results-appbar">
              <h1>Résultats</h1>
              <button aria-label="Paramètres">
                <Icon>settings</Icon>
              </button>
            </header>

            <main className="results-content">
              <section className="results-hero-card">
                <span className="results-confetti confetti-one" />
                <span className="results-confetti confetti-two" />
                <span className="results-confetti confetti-three" />
                <span className="results-confetti confetti-four" />
                <div className="results-score-ring" aria-label="Score : 85%">
                  <div>
                    <strong>85%</strong>
                    <span>Score</span>
                  </div>
                </div>
                <div className="results-hero-copy">
                  <span>
                    <Icon>workspace_premium</Icon>
                    QUIZ TERMINÉ
                  </span>
                  <h2>Excellent travail !</h2>
                  <p>Vos efforts portent leurs fruits. Chaque quiz renforce vos connaissances.</p>
                </div>
              </section>

              <section className="results-card performance-results-card">
                <div className="results-section-title">
                  <div>
                    <span>
                      <Icon>analytics</Icon>
                    </span>
                    <h2>Votre performance</h2>
                  </div>
                  <small>Mathématiques</small>
                </div>
                <div className="results-stat-grid">
                  <article>
                    <span className="correct">
                      <Icon>check_circle</Icon>
                    </span>
                    <div>
                      <strong>17</strong>
                      <small>Bonnes réponses</small>
                    </div>
                  </article>
                  <article>
                    <span className="wrong">
                      <Icon>cancel</Icon>
                    </span>
                    <div>
                      <strong>3</strong>
                      <small>Mauvaises réponses</small>
                    </div>
                  </article>
                  <article>
                    <span className="time">
                      <Icon>schedule</Icon>
                    </span>
                    <div>
                      <strong>09:18</strong>
                      <small>Temps passé</small>
                    </div>
                  </article>
                  <article>
                    <span className="accuracy">
                      <Icon>target</Icon>
                    </span>
                    <div>
                      <strong>85%</strong>
                      <small>Précision</small>
                    </div>
                  </article>
                </div>
              </section>

              <section className="results-card comparison-card">
                <div className="results-section-title">
                  <div>
                    <span>
                      <Icon>trending_up</Icon>
                    </span>
                    <h2>Votre progression</h2>
                  </div>
                  <span className="improvement-badge">
                    <Icon>north_east</Icon>
                    +13%
                  </span>
                </div>
                <div className="comparison-chart">
                  <div>
                    <span>Quiz précédent</span>
                    <strong>72%</strong>
                    <div className="comparison-track previous">
                      <i />
                    </div>
                  </div>
                  <div>
                    <span>Quiz actuel</span>
                    <strong>85%</strong>
                    <div className="comparison-track current">
                      <i />
                    </div>
                  </div>
                </div>
                <p>
                  <Icon>auto_awesome</Icon> Vous avez progressé depuis votre dernier quiz.
                </p>
              </section>

              <section className="results-card review-topics-card">
                <div className="results-section-title">
                  <div>
                    <span>
                      <Icon>menu_book</Icon>
                    </span>
                    <h2>À revoir</h2>
                  </div>
                  <small>3 notions</small>
                </div>
                <p>Quelques minutes de révision suffiront pour consolider ces notions.</p>
                <div className="review-topic-list">
                  {[
                    ["pie_chart", "Fractions", "2 erreurs"],
                    ["functions", "Équations", "1 erreur"],
                    ["architecture", "Géométrie", "À renforcer"],
                  ].map(([icon, topic, detail]) => (
                    <article key={topic}>
                      <span>
                        <Icon>{icon}</Icon>
                      </span>
                      <div>
                        <strong>{topic}</strong>
                        <small>{detail}</small>
                      </div>
                      <button onClick={() => setScreen("programme")}>Réviser</button>
                    </article>
                  ))}
                </div>
              </section>

              <section className="teacher-recommendation-card">
                <div className="results-teacher-avatar" aria-hidden="true">
                  <span className="results-avatar-hair" />
                  <span className="results-avatar-face" />
                  <span className="results-avatar-body">
                    <Icon>school</Icon>
                  </span>
                </div>
                <div>
                  <span>CONSEIL DE PROFESSEURE AMINA</span>
                  <p>Je vous conseille de revoir les fractions avant de passer au prochain quiz.</p>
                </div>
                <Icon className="recommendation-quote">format_quote</Icon>
              </section>

              <section className="results-card achievements-card">
                <div className="results-section-title">
                  <div>
                    <span>
                      <Icon>emoji_events</Icon>
                    </span>
                    <h2>Badges obtenus</h2>
                  </div>
                  <small>3 nouveaux</small>
                </div>
                <div className="achievement-list">
                  <article>
                    <span className="badge-blue">
                      <Icon>flag</Icon>
                    </span>
                    <strong>Premier quiz terminé</strong>
                  </article>
                  <article>
                    <span className="badge-orange">
                      <Icon>local_fire_department</Icon>
                    </span>
                    <strong>Série de 5 jours</strong>
                  </article>
                  <article>
                    <span className="badge-green">
                      <Icon>workspace_premium</Icon>
                    </span>
                    <strong>Score supérieur à 80%</strong>
                  </article>
                </div>
              </section>

              <section className="results-actions">
                <button className="results-primary-action" onClick={() => setScreen("programme")}>
                  Continuer l’apprentissage
                  <Icon>arrow_forward</Icon>
                </button>
                <button className="results-secondary-action" onClick={() => setScreen("programme")}>
                  <Icon>menu_book</Icon>
                  Revoir la leçon
                </button>
                <button className="results-text-action" onClick={startQuiz}>
                  <Icon>replay</Icon>
                  Recommencer le quiz
                </button>
              </section>
            </main>

            <BottomNavigation active="evaluation" onNavigate={setScreen} />
          </div>
        )}

        {screen === "chat" && (
          <div className="screen chat-screen">
            <header className="chat-appbar">
              <button
                className="chat-appbar-button"
                onClick={() => setScreen("teacher")}
                aria-label="Retour"
              >
                <Icon>arrow_back</Icon>
              </button>
              <h1>Chat</h1>
              <div>
                <button className="chat-appbar-button" aria-label="Rechercher">
                  <Icon>search</Icon>
                </button>
                <button className="chat-appbar-button" aria-label="Paramètres">
                  <Icon>settings</Icon>
                </button>
              </div>
            </header>

            <section className="chat-teacher-header">
              <span className="chat-teacher-avatar" aria-hidden="true">
                <i className="avatar-hair" />
                <i className="avatar-face" />
                <i className="avatar-body" />
              </span>
              <div>
                <strong>Professeure Amina</strong>
                <span>
                  <i />
                  Prête à vous aider
                </span>
              </div>
              <span className="offline-chat-badge">
                <Icon>cloud_off</Icon>
                Hors ligne
              </span>
            </section>

            <main className="chat-thread" aria-live="polite">
              <div className="chat-day-label">
                <span>Aujourd’hui</span>
              </div>

              {chatMessages.length === 0 && (
                <section className="chat-suggestions">
                  <div>
                    <span className="suggestion-spark">
                      <Icon>auto_awesome</Icon>
                    </span>
                    <h2>Que souhaitez-vous apprendre ?</h2>
                    <p>Professeure Amina est prête à vous accompagner.</p>
                  </div>
                  <div className="suggestion-grid">
                    {[
                      ["lightbulb", "Expliquer une leçon"],
                      ["calculate", "Résoudre un exercice"],
                      ["quiz", "Préparer un quiz"],
                      ["summarize", "Résumer un chapitre"],
                    ].map(([icon, label]) => (
                      <button key={label} onClick={() => sendChatMessage(label)}>
                        <Icon>{icon}</Icon>
                        <span>{label}</span>
                      </button>
                    ))}
                  </div>
                </section>
              )}

              {chatMessages.map((message) =>
                message.role === "student" ? (
                  <div className="student-message-row" key={message.id}>
                    <div className="student-message">
                      <p>{message.text}</p>
                      <span>
                        10:24 <Icon>done_all</Icon>
                      </span>
                    </div>
                  </div>
                ) : (
                  <div className="teacher-message-group" key={message.id}>
                    <div className="message-avatar" aria-hidden="true">
                      <Icon>school</Icon>
                    </div>
                    <div className="teacher-message-content">
                      <span className="message-author">Professeure Amina</span>
                      <article className="teacher-message">
                        <p>{message.text}</p>
                        <ul>
                          <li>
                            Le côté le plus long s’appelle <mark>l’hypoténuse</mark>.
                          </li>
                          <li>
                            La formule est <mark>a² + b² = c²</mark>.
                          </li>
                        </ul>
                        <div className="learning-example">
                          <span>
                            <Icon>tips_and_updates</Icon>
                          </span>
                          <div>
                            <strong>Exemple rapide</strong>
                            <p>Si a = 3 et b = 4, alors c = 5.</p>
                          </div>
                        </div>
                        <div className="source-card">
                          <span>
                            <Icon>menu_book</Icon>
                          </span>
                          <div>
                            <small>Source</small>
                            <strong>Mathématiques · 9e année</strong>
                            <p>Chapitre 5 · Page 42</p>
                          </div>
                          <Icon>verified</Icon>
                        </div>
                      </article>
                      <div className="message-actions">
                        <button
                          aria-label="Copier"
                          onClick={() => navigator.clipboard?.writeText(message.text)}
                        >
                          <Icon>content_copy</Icon>
                        </button>
                        <button
                          aria-label="Lire à voix haute"
                          onClick={() => readChatMessage(message.text)}
                        >
                          <Icon>volume_up</Icon>
                        </button>
                        <button
                          aria-label="Régénérer"
                          onClick={() =>
                            setChatMessages((current) =>
                              current.map((item) =>
                                item.id === message.id
                                  ? {
                                      ...item,
                                      text: "Regardons autrement : imaginez un carré construit sur chaque côté du triangle rectangle. Les deux petits carrés remplissent exactement le grand.",
                                    }
                                  : item,
                              ),
                            )
                          }
                        >
                          <Icon>refresh</Icon>
                        </button>
                        <span />
                        <button
                          className={messageReaction === "like" ? "selected" : ""}
                          aria-label="J’aime"
                          onClick={() => setMessageReaction("like")}
                        >
                          <Icon>thumb_up</Icon>
                        </button>
                        <button
                          className={messageReaction === "dislike" ? "selected" : ""}
                          aria-label="Je n’aime pas"
                          onClick={() => setMessageReaction("dislike")}
                        >
                          <Icon>thumb_down</Icon>
                        </button>
                      </div>
                    </div>
                  </div>
                ),
              )}
            </main>

            <div className="chat-composer">
              <button
                className={microphoneActive ? "active" : ""}
                onClick={() => setMicrophoneActive((current) => !current)}
                aria-label="Microphone"
              >
                <Icon>{microphoneActive ? "mic" : "mic_none"}</Icon>
              </button>
              <input
                value={chatInput}
                onChange={(event) => setChatInput(event.target.value)}
                onKeyDown={(event) => {
                  if (event.key === "Enter") sendChatMessage();
                }}
                placeholder="Posez une question à votre professeur..."
                aria-label="Votre question"
              />
              <button
                className="chat-send-button"
                onClick={() => sendChatMessage()}
                disabled={!chatInput.trim()}
                aria-label="Envoyer"
              >
                <Icon>arrow_upward</Icon>
              </button>
            </div>

            <BottomNavigation active="chat" onNavigate={setScreen} />
          </div>
        )}
      </section>
    </main>
  );
}
