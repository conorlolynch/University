pterm(null).
pterm(f0(X)) :- pterm(X).
pterm(f1(X)) :- pterm(X).


/* Problem 1:  incr(P1, P2) */

incr(null, f1(null)).
incr(f0(X), f1(X)).
incr(f1(X), f0(Y)) :- incr(X, Y).


/* Problem 2:  legal(P) */

legal(f0(null)).
legal(f1(null)).
legal(X) :- legal(Y), (X = f0(Y) ; X = f1(Y)), Y \= f0(null).



/* incR(X, Y) */
incR(X, Y) :- legal(X), incr(X, Y).


/* Problem 3: add(X, Y, Z) */
add(null, X, X).
add(X, null, X).
add(f0(null), X, X).
add(X, f0(null), X).
add(X, Y, Z) :- incr(A, X), add(A, Y, B), incr(B, Z).


/* Problem 4:  mult(X, Y, Z)  */
mult(null, _, null).
mult(_, null, null).
mult(f0(null), _, f0(null)).
mult(_, f0(null), f0(null)).
mult(X, Y, Z) :- incr(A, X), mult(A, Y, B), add(B, Y, Z).


/* Problem 5:  revers(P, RevP)  */
revers(X,RX) :- revers(X,null,RX).
revers(null, X, X).
revers(f0(X), T ,H) :- revers(X,f0(T),H).
revers(f1(X), T, H) :- revers(X,f1(T),H).



/* Problem 6:  normalize(P, Pn) */
normalize(null,f0(null)).
normalize(f0(null),f0(null)).
normalize(X, Y) :- revers(X, Z), normalize2(Z, A), revers(A, Y).
normalize2(null,f0(null)).
normalize2(f1(X), f1(X)).
normalize2(f0(X), Y) :- normalize2(X, Y).



% test add inputting numbers N1 and N2
testAdd(N1,N2,T1,T2,Sum,SumT) :- numb2pterm(N1,T1), numb2pterm(N2,T2),
add(T1,T2,SumT), pterm2numb(SumT,Sum).

% test mult inputting numbers N1 and N2
testMult(N1,N2,T1,T2,N1N2,T1T2) :- numb2pterm(N1,T1), numb2pterm(N2,T2),
mult(T1,T2,T1T2), pterm2numb(T1T2,N1N2).

% test revers inputting list L
testRev(L,Lr,T,Tr) :- ptermlist(T,L), revers(T,Tr), ptermlist(Tr,Lr).

% test normalize inputting list L
testNorm(L,T,Tn,Ln) :- ptermlist(T,L), normalize(T,Tn), ptermlist(Tn,Ln).

% make a pterm T from a number N numb2term(+N,?T)
numb2pterm(0,f0(null)).
numb2pterm(N,T) :- N>0, M is N-1, numb2pterm(M,Temp), incr(Temp,T).

% make a number N from a pterm T pterm2numb(+T,?N)
pterm2numb(null,0).
pterm2numb(f0(X),N) :- pterm2numb(X,M), N is 2*M.
pterm2numb(f1(X),N) :- pterm2numb(X,M), N is 2*M +1.

% reversible ptermlist(T,L)
ptermlist(null,[]).
ptermlist(f0(X),[0|L]) :- ptermlist(X,L).
ptermlist(f1(X),[1|L]) :- ptermlist(X,L).
